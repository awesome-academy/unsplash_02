package com.sun.unsplash_02.data.source.local

import android.content.Context
import android.text.TextUtils

class SearchHistoryPreference(context: Context) {

    private val sharedPreference =
        context.getSharedPreferences(PREFERENCE_SEARCH_HISTORY, Context.MODE_PRIVATE)

    private fun saveHistory(listHistory: MutableList<String>) {
        sharedPreference.edit().apply {
            putString(KEY_HISTORY, listHistory.joinToString())
            apply()
        }
    }

    fun addHistory(history: String) {
        getListHistory().toMutableList().let {
            if (!it.contains(history)) {
                if (it.size >= MAX_SIZE) {
                    it.run {
                        removeAt(size - 1)
                        add(history)
                    }
                } else {
                    it.add(history)
                }
            }
            saveHistory(it)
        }
    }

    fun getListHistory(): List<String> {
        sharedPreference.getString(KEY_HISTORY, "")?.let {
            if (!TextUtils.isEmpty(it)) {
                return it.split(",")
            }
        }
        return emptyList()
    }

    companion object {
        private const val PREFERENCE_SEARCH_HISTORY = "PREFERENCE_SEARCH_HISTORY"
        private const val KEY_HISTORY = "KEY_HISTORY"
        private const val MAX_SIZE = 5

        @Volatile
        private var instance: SearchHistoryPreference? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: (SearchHistoryPreference(context)).also {
                instance = it
            }
        }
    }
}
