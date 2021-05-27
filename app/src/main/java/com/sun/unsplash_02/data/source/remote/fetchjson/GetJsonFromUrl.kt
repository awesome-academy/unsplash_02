package com.sun.unsplash_02.data.source.remote.fetchjson

import android.os.AsyncTask
import com.sun.unsplash_02.data.source.remote.OnFetchDataJsonListener

class GetJsonFromUrl<T> constructor(
    private val listener: OnFetchDataJsonListener<T>,
    private val keyEntity: String
) : AsyncTask<String?, Void?, T?>() {

    private var exception: Exception? = null


    override fun doInBackground(vararg strings: String?): T? {
        var data: T? = null
        try {
            val parseDataWithJson = ParseDataWithJson()
            val json = parseDataWithJson.getJsonFromUrl(strings[0]).toString()
            data = ParseDataWithJson().parseJsonToData(json, keyEntity) as T
        } catch (e: Exception) {
            exception = e
        }
        return data
    }

    override fun onPostExecute(result: T?) {
        super.onPostExecute(result)
        result?.let {
            listener.onSuccess(it)
        } ?: listener.onError(exception)
    }
}
