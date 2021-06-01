package com.sun.unsplash_02.data.source.local

import android.content.Context
import com.sun.unsplash_02.data.source.ImageDataSource

class ImageLocalDataSource constructor(
    private val searchHistoryPreference: SearchHistoryPreference
) : ImageDataSource.Local {

    override fun addSearchHistory(history: String) = searchHistoryPreference.addHistory(history)

    override fun getSearchHistory() = searchHistoryPreference.getListHistory()

    companion object {
        @Volatile
        private var instance: ImageLocalDataSource? = null

        fun getInstance(searchHistoryPreference: SearchHistoryPreference) = instance ?: synchronized(this) {
            instance ?: ImageLocalDataSource(searchHistoryPreference).also {
                instance = it
            }
        }
    }
}
