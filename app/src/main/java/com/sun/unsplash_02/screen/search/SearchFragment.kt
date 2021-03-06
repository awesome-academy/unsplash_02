package com.sun.unsplash_02.screen.search

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.data.source.local.ImageLocalDataSource
import com.sun.unsplash_02.data.source.local.SearchHistoryPreference
import com.sun.unsplash_02.data.source.remote.ImageRemoteDataResource
import com.sun.unsplash_02.screen.detail.DetailFragment
import com.sun.unsplash_02.screen.home.adapter.HistorySearchAdapter
import com.sun.unsplash_02.screen.home.adapter.PhotoAdapter
import com.sun.unsplash_02.utils.*
import com.sun.unsplash_02.utils.extension.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : BaseFragment(), SearchContract.View {

    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter {
            handlePhotoClick(it)
        }
    }
    private val historySearchAdapter: HistorySearchAdapter by lazy {
        HistorySearchAdapter {
            handleHistoryClick(it)
        }
    }
    private lateinit var searchPresenter: SearchPresenter
    private var currentSearchText: String? = null
    private var isLoading = false

    override fun getLayoutResourceId() = R.layout.fragment_search

    override fun initView(view: View) {
        activity?.let { view.editTextSearch.showKeyboard(it) }
        view.toolbarSearch.run {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.let {
                    it.supportFragmentManager.popBackStack()
                    view.editTextSearch.hideKeyboard(it)
                }
            }
        }
        view.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recyclerSearch.toGone()
                recyclerHistorySearch.toVisible()
                val listHistory = searchPresenter.getHistory().toMutableList()
                if (listHistory.size > 0)
                    historySearchAdapter.setListHistory(listHistory)
            } else {
                recyclerHistorySearch.toGone()
                recyclerSearch.toVisible()
            }
        }
        view.imageSearch.setOnClickListener {
            val query = editTextSearch.text.toString().trim()
            if (!TextUtils.isEmpty(query)) {
                if (query == currentSearchText) {
                    searchPresenter.searchImage(query)
                } else {
                    currentSearchText = query
                    photoAdapter.clear()
                    recyclerSearch.smoothScrollToPosition(0)
                    searchPresenter.apply {
                        resetPage()
                        searchImage(query)
                    }
                }
                activity?.let { editTextSearch.hideKeyboard(it) }
                searchPresenter.addHistory(query)
            } else {
                Toast.makeText(context, getString(R.string.msg_empty_search), Toast.LENGTH_LONG)
                    .show()
            }
        }
        view.recyclerHistorySearch.adapter = historySearchAdapter
        view.recyclerSearch.run {
            adapter = photoAdapter
            smoothScrollToPosition(0)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerSearch.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (!isLoading && totalItemCount <= lastVisibleItem + Constants.VISIBLE_THRESHOLD) {
                        loadMoreData()
                        isLoading = true
                    }
                }
            })
        }
    }

    override fun initData() {
        SearchPresenter(
            ImageRepository.getInstance(
                ImageRemoteDataResource.getInstance(),
                ImageLocalDataSource.getInstance(SearchHistoryPreference.getInstance(requireContext()))
            )
        ).run {
            setView(this@SearchFragment)
            onStart()
            searchPresenter = this
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.onStop()
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        hideProgressDialog()
    }

    override fun onImageLoaded(images: MutableList<Image?>) {
        if (isLoading) {
            photoAdapter.stopLoadMore()
            isLoading = false
        }
        if (images.size < 1) {
            Toast.makeText(context, getString(R.string.not_found_result), Toast.LENGTH_LONG).show()
        }
        photoAdapter.setListPhotos(images)
    }

    override fun onError(e: Exception?) {
        Toast.makeText(context, e?.message, Toast.LENGTH_LONG).show()
    }

    private fun handlePhotoClick(image: Image) =
        addFragment(R.id.frameMainContainer, DetailFragment.newInstance(image))

    private fun handleHistoryClick(history: String) = editTextSearch.run {
        setText(history)
        moveCursorToEnd()
    }

    private fun loadMoreData() {
        photoAdapter.startLoadMore()
        searchPresenter.searchImage(editTextSearch.text.toString().trim())
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
