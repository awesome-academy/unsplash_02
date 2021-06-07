package com.sun.unsplash_02.utils

import android.graphics.Bitmap
import android.graphics.Color

object ImageFilters {

    fun applyHighlightEffect(src: Bitmap): Bitmap? {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int
        var pixelColor: Int
        val height = src.height
        val width = src.width
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixelColor = src.getPixel(x, y)
                alpha = Color.alpha(pixelColor)
                red = (Color.red(pixelColor) * 0.8).toInt()
                green = (Color.green(pixelColor) * 1.6).toInt()
                blue = (Color.blue(pixelColor) * 0.8).toInt()
                if (red > 255) {
                    red = 255
                }
                if (red < 0) {
                    red = 0
                }
                if (green > 255) {
                    green = 255
                }
                if (green < 0) {
                    green = 0
                }
                if (blue > 255) {
                    blue = 255
                }
                if (blue < 0) {
                    blue = 0
                }
                bmOut.setPixel(x, y, Color.argb(alpha, red, green, blue))
            }
        }
        return bmOut
    }

    fun applyInvertEffect(src: Bitmap): Bitmap? {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int
        var pixelColor: Int
        val height = src.height
        val width = src.width
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixelColor = src.getPixel(x, y)
                alpha = Color.alpha(pixelColor)
                red = 255 - Color.red(pixelColor)
                green = 255 - Color.green(pixelColor)
                blue = 255 - Color.blue(pixelColor)
                bmOut.setPixel(x, y, Color.argb(alpha, red, green, blue))
            }
        }
        return bmOut
    }

    fun applyGreyscaleEffect(src: Bitmap): Bitmap? {
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int
        var pixel: Int
        val width = src.width
        val height = src.height
        for (x in 0 until width) {
            for (y in 0 until height) {
                pixel = src.getPixel(x, y)
                alpha = Color.alpha(pixel)
                red = Color.red(pixel)
                green = Color.green(pixel)
                blue = Color.blue(pixel)
                blue = (GS_RED * red + GS_GREEN * green + GS_BLUE * blue).toInt()
                green = blue
                red = green
                bmOut.setPixel(x, y, Color.argb(alpha, red, green, blue))
            }
        }
        return bmOut
    }

    private const val GS_RED = 0.299
    private const val GS_GREEN = 0.587
    private const val GS_BLUE = 0.114
}
