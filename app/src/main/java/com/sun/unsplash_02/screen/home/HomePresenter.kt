package com.sun.unsplash_02.screen.home

import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.data.source.remote.OnFetchDataJsonListener

class HomePresenter constructor(private val imageRepository: ImageRepository) :
    HomeContract.Presenter {

    private var view: HomeContract.View? = null

    override fun loadListCollections() =
        imageRepository.getCollections(object : OnFetchDataJsonListener<MutableList<Collection>> {
            override fun onSuccess(data: MutableList<Collection>) {
                view?.onCollectionLoaded(data)
                view?.hideLoading()
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
                view?.hideLoading()
            }
        })

    override fun loadListImages(page: Int) =
        imageRepository.getImages(object : OnFetchDataJsonListener<MutableList<Image>> {
            override fun onSuccess(data: MutableList<Image>) {
            }

            override fun onError(exception: Exception?) {
            }
        }, page)

    override fun onStart() {
        view?.showLoading()
        loadListCollections()
    }

    override fun onStop() {
        view = null
    }

    override fun setView(view: HomeContract.View?) {
        this.view = view
    }
}
