package com.sun.unsplash_02.utils

import android.graphics.*
import android.widget.ImageView

class BrightnessThread constructor(
    private val imageView: ImageView,
    private val bitmap: Bitmap?
) : Thread() {

    private var tempBitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var paint: Paint? = null

    private var isRunning = false

    init {
        bitmap?.let {
            tempBitmap = Bitmap.createBitmap(
                it.width,
                it.height,
                it.config
            ).also { temp ->
                canvas = Canvas(temp)
                paint = Paint()
            }
        }
    }

    fun adjustBrightness(amount: Int) {
        ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, amount.toFloat(),
                0f, 1f, 0f, 0f, amount.toFloat(),
                0f, 0f, 1f, 0f, amount.toFloat(),
                0f, 0f, 0f, 1f, 0f
            )
        ).also {
            paint?.colorFilter = ColorMatrixColorFilter(it)
            isRunning = true
        }
    }

    override fun run() {
        while (true) {
            if (isRunning) {
                bitmap?.let {
                    canvas?.drawBitmap(it, 0f, 0f, paint)
                    imageView.post {
                        imageView.setImageBitmap(tempBitmap)
                        isRunning = false
                    }
                }
            }
        }
    }
}
