package com.sun.unsplash_02.screen.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.data.source.local.ImageLocalDataSource
import com.sun.unsplash_02.data.source.local.SearchHistoryPreference
import com.sun.unsplash_02.data.source.remote.ImageRemoteDataResource
import com.sun.unsplash_02.screen.detail.DetailFragment
import com.sun.unsplash_02.screen.home.adapter.CollectionAdapter
import com.sun.unsplash_02.screen.home.adapter.PhotoAdapter
import com.sun.unsplash_02.screen.search.SearchFragment
import com.sun.unsplash_02.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment(), HomeContract.View {

    private val collectionAdapter: CollectionAdapter by lazy {
        CollectionAdapter {
            handleCollectionClick(it)
        }
    }
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter {
            handlePhotoClick(it)
        }
    }

    private var currentCollectionId: String? = null
    private var selectedType = TYPE_PHOTO
    private var isLoading = false

    private lateinit var homePresenter: HomePresenter

    override fun getLayoutResourceId() = R.layout.fragment_home

    override fun initView(view: View) {
        view.recyclerCollection.adapter = collectionAdapter
        view.recyclerPhoto.run {
            adapter = photoAdapter
            smoothScrollToPosition(0)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerPhoto.layoutManager as GridLayoutManager
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
        (activity as AppCompatActivity).setSupportActionBar(toolbarMain)
        setHasOptionsMenu(true)
        HomePresenter(
            ImageRepository.getInstance(
                ImageRemoteDataResource.getInstance(),
                ImageLocalDataSource.getInstance(SearchHistoryPreference(requireContext()))
            )
        ).run {
            setView(this@HomeFragment)
            onStart()
            homePresenter = this
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSearch -> {
                addFragment(R.id.frameMainContainer, SearchFragment.newInstance())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.onStop()
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        hideProgressDialog()
    }

    override fun onCollectionLoaded(collections: MutableList<Collection>) {
        collectionAdapter.setListCollections(collections)
    }

    override fun onImageLoaded(images: MutableList<Image?>) {
        if (isLoading) {
            photoAdapter.stopLoadMore()
            isLoading = false
        }
        photoAdapter.setListPhotos(images)
    }

    override fun onError(e: Exception?) {
        Toast.makeText(context, e?.message, Toast.LENGTH_LONG).show()
    }

    private fun handleCollectionClick(collection: Collection) {
        if (isLoading) {
            photoAdapter.stopLoadMore()
            isLoading = false
        }
        if (currentCollectionId != collection.id) {
            homePresenter.resetCurrentPage()
            recyclerPhoto.smoothScrollToPosition(0)
        }
        currentCollectionId = collection.id
        selectedType = TYPE_PHOTO_COLLECTION
        photoAdapter.clear()
        homePresenter.loadListImagesByCollection(collection.id)
    }

    private fun handlePhotoClick(image: Image) {
        addFragment(R.id.frameMainContainer, DetailFragment.newInstance(image))
    }

    private fun loadMoreData() {
        photoAdapter.startLoadMore()
        if (selectedType == TYPE_PHOTO) {
            homePresenter.loadListImages()
        } else {
            currentCollectionId?.let { id ->
                homePresenter.loadListImagesByCollection(id)
            }
        }
    }

    companion object {
        private const val TYPE_PHOTO_COLLECTION = 1
        private const val TYPE_PHOTO = 2

        fun newInstance() = HomeFragment()
    }
}
