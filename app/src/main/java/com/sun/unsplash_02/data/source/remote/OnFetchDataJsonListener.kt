package com.sun.unsplash_02.data.source.remote

interface OnFetchDataJsonListener<T> {
    fun onSuccess(data: T)
    fun onError(exception: Exception?)
}
