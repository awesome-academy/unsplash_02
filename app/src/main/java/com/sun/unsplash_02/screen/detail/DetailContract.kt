package com.sun.unsplash_02.screen.detail

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.sun.unsplash_02.base.BasePresenter
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.utils.customview.ZoomImageView

interface DetailContract {

    interface View {
        fun onStartDownload()
        fun onUpdateProgressBar(value: Int)
        fun onComplete(imagePath: String, type: Int)
        fun onError(e: Exception?)
    }

    interface Presenter : BasePresenter<View> {
        fun loadImage(imageView: ZoomImageView, imageUrl: String)
        fun downloadImage(context: Context, image: Image, type: Int)
    }
}
