package com.sun.unsplash_02.utils.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.getBitmapFromView(): Bitmap? {
    val bitmap =
        Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    this.draw(Canvas(bitmap))
    return bitmap
}
