package com.sun.unsplash_02.screen.detail

import android.content.Context
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.utils.customview.ZoomImageView
import com.sun.unsplash_02.utils.task.DownloadImageTask
import com.sun.unsplash_02.utils.task.LoadImageTask
import com.sun.unsplash_02.utils.task.OnDownloadListener

class DetailPresenter : DetailContract.Presenter {

    private var view: DetailContract.View? = null

    override fun onStart() {
    }

    override fun loadImage(imageView: ZoomImageView, imageUrl: String) {
        LoadImageTask(imageView).execute(imageUrl)
    }

    override fun downloadImage(context: Context, image: Image, type: Int) {
        DownloadImageTask(context, object : OnDownloadListener {
            override fun onStart() {
                view?.onStartDownload()
            }

            override fun onComplete(imagePath: String) {
                view?.onComplete(imagePath, type)
            }

            override fun onProgressUpdate(value: Int) {
                view?.onUpdateProgressBar(value)
            }

            override fun onError(exception: Exception?) {
                view?.onError(exception)
            }

        }).execute(image)
    }

    override fun onStop() {
        view = null
    }

    override fun setView(view: DetailContract.View?) {
        this.view = view
    }
}
