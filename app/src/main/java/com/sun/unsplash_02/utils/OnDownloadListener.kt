package com.sun.unsplash_02.utils

import java.lang.Exception

interface OnDownloadListener {
    fun onStart()
    fun onComplete(imagePath: String)
    fun onProgressUpdate(value: Int)
    fun onError(exception: Exception?)
}
