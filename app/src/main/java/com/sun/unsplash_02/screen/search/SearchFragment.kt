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
import com.sun.unsplash_02.screen.home.adapter.PhotoAdapter
import com.sun.unsplash_02.screen.main.MainActivity
import com.sun.unsplash_02.utils.hideKeyboard
import com.sun.unsplash_02.utils.showKeyboard
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), SearchContract.View, PhotoAdapter.ItemClickListener {

    private lateinit var searchPresenter: SearchPresenter
    private var photoAdapter: PhotoAdapter? = null
    private var isLoading = false

    override fun getLayoutResourceId() = R.layout.fragment_search

    override fun onViewCreated(view: View) {
        SearchPresenter(ImageRepository.getInstance()).run {
            setView(this@SearchFragment)
            onStart()
            searchPresenter = this
        }
    }

    override fun onInit() {
        activity?.let { edtSearch.showKeyboard(it) }
        toolbarSearch.setNavigationIcon(R.drawable.ic_arrow_back)
        PhotoAdapter().run {
            photoAdapter = this
            recyclerSearch.adapter = this
            setItemClickListener(this@SearchFragment)
        }
        super.onInit()
    }

    override fun onEvent() {
        toolbarSearch.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        imageSearch.setOnClickListener {
            val query = edtSearch.text.toString().trim()
            if (!TextUtils.isEmpty(query)) {
                searchPresenter.searchImage(query)
                activity?.let { edtSearch.hideKeyboard(it) }
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
                if (!isLoading) {
                    photoAdapter?.let {
                        if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                            it.getListPhotos().size - 1
                        ) {
                            recyclerView.post {
                                it.startLoadMore()
                            }
                            searchPresenter.searchImage(edtSearch.text.toString().trim())
                            isLoading = true
                        }
                    }
                }
            }
        })
        super.onEvent()
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

    override fun onItemClick(image: Image?) {
        Toast.makeText(context, image?.urls?.full, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
