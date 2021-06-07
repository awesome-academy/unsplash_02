package com.sun.unsplash_02.utils.customview

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

class PhotoDrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var imageSource: ImageView? = null
    private var drawingView: DrawingView? = null

    init {
        imageSource = ImageView(context).apply {
            id = IMAGE_DRAW_ID
            adjustViewBounds = true
        }
        val imgSrcParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        }
        drawingView = DrawingView(context)
        val drawParam = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            addRule(ALIGN_TOP, IMAGE_DRAW_ID)
            addRule(ALIGN_BOTTOM, IMAGE_DRAW_ID)
        }
        addView(imageSource, imgSrcParam)
        addView(drawingView, drawParam)
    }

    fun setImageDraw(imageUri: Uri) = imageSource?.setImageURI(imageUri)

    companion object {
        private const val IMAGE_DRAW_ID = 1
    }
}
