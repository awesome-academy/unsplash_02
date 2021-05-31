package com.sun.unsplash_02.screen.search

import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.data.source.remote.OnFetchDataJsonListener

class SearchPresenter constructor(private val imageRepository: ImageRepository) :
    SearchContract.Presenter {

    private var view: SearchContract.View? = null

    override fun searchImage(query: String) {
        view?.showLoading()
        imageRepository.searchImage(object : OnFetchDataJsonListener<MutableList<Image?>> {
            override fun onSuccess(data: MutableList<Image?>) {
                view?.run {
                    hideLoading()
                    onImageLoaded(data)
                }
            }

            override fun onError(exception: Exception?) {
                view?.run {
                    hideLoading()
                    onError(exception)
                }
            }

        }, query, ++currentPage)
    }

    override fun onStart() {
    }

    override fun onStop() {
        view = null
    }

    override fun setView(view: SearchContract.View?) {
        this.view = view
    }

    companion object {
        var currentPage = 0
    }
}
