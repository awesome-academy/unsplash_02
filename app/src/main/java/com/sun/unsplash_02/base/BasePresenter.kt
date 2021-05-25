package com.sun.unsplash_02.base

interface BasePresenter<T> {
    fun onStart()
    fun onStop()
    fun setView(view: T?)
}
