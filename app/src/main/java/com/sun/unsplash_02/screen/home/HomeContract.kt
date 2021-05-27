package com.sun.unsplash_02.screen.home

import com.sun.unsplash_02.base.BasePresenter
import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image

interface HomeContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun onCollectionLoaded(collections: MutableList<Collection>)
        fun onImageLoaded(images: MutableList<Image?>)
        fun onError(e: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun loadListCollections()
        fun loadListImages()
        fun loadListImagesByCollection(collectionId: String)
    }
}
