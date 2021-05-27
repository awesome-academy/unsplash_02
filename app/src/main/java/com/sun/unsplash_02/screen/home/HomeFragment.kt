package com.sun.unsplash_02.screen.home

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.screen.home.adapter.CollectionAdapter
import com.sun.unsplash_02.screen.home.adapter.PhotoAdapter
import com.sun.unsplash_02.screen.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), HomeContract.View, PhotoAdapter.ItemClickListener {

    private var collectionAdapter: CollectionAdapter? = null
    private var photoAdapter: PhotoAdapter? = null
    private var currentCollectionId: String? = null
    private var selectedType = TYPE_PHOTO
    private var isLoading = false

    private lateinit var homePresenter: HomePresenter

    override fun getLayoutResourceId() = R.layout.fragment_home

    override fun onViewCreated(view: View) {
        HomePresenter(ImageRepository.getInstance()).run {
            setView(this@HomeFragment)
            onStart()
            homePresenter = this
        }
    }

    override fun onInit() {
        CollectionAdapter().run {
            setOnItemClickListener {
                if (isLoading) {
                    photoAdapter?.stopLoadMore()
                    isLoading = false
                }
                showLoading()
                if (currentCollectionId != it.id) {
                    homePresenter.resetCurrentPage()
                    recyclerPhoto.smoothScrollToPosition(0)
                }
                currentCollectionId = it.id
                selectedType = TYPE_PHOTO_COLLECTION
                photoAdapter?.clear()
                homePresenter.loadListImagesByCollection(it.id)
            }
            collectionAdapter = this
            recyclerCollection.adapter = this
        }
        PhotoAdapter().run {
            setItemClickListener(this@HomeFragment)
            photoAdapter = this
            recyclerPhoto.adapter = this
        }
        super.onInit()
    }

    override fun onEvent() {
        recyclerPhoto.run {
            smoothScrollToPosition(0)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerPhoto.layoutManager as GridLayoutManager
                    if (!isLoading) {
                        photoAdapter?.let {
                            if (layoutManager.findLastCompletelyVisibleItemPosition() ==
                                it.getListPhotos().size - 1
                            ) {
                                recyclerView.post {
                                    it.startLoadMore()
                                }
                                if (selectedType == TYPE_PHOTO) {
                                    homePresenter.loadListImages()
                                } else {
                                    currentCollectionId?.let { id ->
                                        homePresenter.loadListImagesByCollection(id)
                                    }
                                }
                                isLoading = true
                            }
                        }
                    }
                }
            })
        }
        super.onEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.onStop()
    }

    override fun showLoading() {
        (getBaseActivity() as MainActivity).showDialogLoading()
    }

    override fun hideLoading() {
        (getBaseActivity() as MainActivity).hideDialogLoading()
    }

    override fun onCollectionLoaded(collections: MutableList<Collection>) {
        collectionAdapter?.setListCollections(collections)
    }

    override fun onImageLoaded(images: MutableList<Image?>) {
        if (isLoading) {
            photoAdapter?.stopLoadMore()
            isLoading = false
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
        private const val TYPE_PHOTO_COLLECTION = 1
        private const val TYPE_PHOTO = 2

        fun newInstance() = HomeFragment()
    }
}
