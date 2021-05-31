package com.sun.unsplash_02.screen.search

import com.sun.unsplash_02.base.BasePresenter
import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image

interface SearchContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun onImageLoaded(images: MutableList<Image?>)
        fun onError(e: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun searchImage(query: String)
    }
}
