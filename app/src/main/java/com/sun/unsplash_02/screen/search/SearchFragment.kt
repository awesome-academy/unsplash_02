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
import com.sun.unsplash_02.screen.home.adapter.HistorySearchAdapter
import com.sun.unsplash_02.screen.home.adapter.PhotoAdapter
import com.sun.unsplash_02.screen.main.MainActivity
import com.sun.unsplash_02.utils.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), SearchContract.View {

    private lateinit var searchPresenter: SearchPresenter
    private var photoAdapter: PhotoAdapter? = null
    private var historySearchAdapter: HistorySearchAdapter? = null
    private var currentSearchText: String? = null
    private var isLoading = false

    override fun getLayoutResourceId() = R.layout.fragment_search

    override fun onViewCreated(view: View) {
        SearchPresenter(
            ImageRepository.getInstance(
                ImageRemoteDataResource.getInstance(),
                ImageLocalDataSource.getInstance(SearchHistoryPreference(requireContext()))
            )
        ).run {
            setView(this@SearchFragment)
            onStart()
            searchPresenter = this
        }
    }

    override fun onInit() {
        activity?.let { editTextSearch.showKeyboard(it) }
        toolbarSearch.setNavigationIcon(R.drawable.ic_arrow_back)
        PhotoAdapter {
        }.run {
            photoAdapter = this
            recyclerSearch.adapter = this
        }
        HistorySearchAdapter {
            editTextSearch.run {
                setText(it)
                moveCursorToEnd()
            }
        }.run {
            historySearchAdapter = this
            recyclerHistorySearch.adapter = this
        }

        editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recyclerSearch.toGone()
                recyclerHistorySearch.toVisible()
                val listHistory = searchPresenter.getHistory().toMutableList()
                if (listHistory.size > 0)
                    historySearchAdapter?.setListHistory(listHistory)
            } else {
                recyclerHistorySearch.toGone()
                recyclerSearch.toVisible()
            }
        }
        super.onInit()
    }

    override fun onEvent() {
        toolbarSearch.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        imageSearch.setOnClickListener {
            val query = editTextSearch.text.toString().trim()
            if (!TextUtils.isEmpty(query)) {
                if (query == currentSearchText) {
                    searchPresenter.searchImage(query)
                } else {
                    currentSearchText = query
                    photoAdapter?.clear()
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
        recyclerSearch.smoothScrollToPosition(0)
        recyclerSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerSearch.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                photoAdapter?.let {
                    if (!isLoading && totalItemCount <= lastVisibleItem + Constants.VISIBLE_THRESHOLD
                    ) {
                        loadMoreData()
                        isLoading = true
                    }
                }

            }
        })
        super.onEvent()
    }

    private fun loadMoreData() {
        photoAdapter?.startLoadMore()
        searchPresenter.searchImage(editTextSearch.text.toString().trim())
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.onStop()
    }

    override fun showLoading() {
        (getBaseActivity() as MainActivity).showDialogLoading()
    }

    override fun hideLoading() {
        (getBaseActivity() as MainActivity).hideDialogLoading()
    }

    override fun onImageLoaded(images: MutableList<Image?>) {
        if (isLoading) {
            photoAdapter?.stopLoadMore()
            isLoading = false
        }
        if (images.size < 1) {
            Toast.makeText(context, getString(R.string.not_found_result), Toast.LENGTH_LONG).show()
        }
        photoAdapter?.setListPhotos(images)
    }

    override fun onError(e: Exception?) {
        Toast.makeText(context, e?.message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
