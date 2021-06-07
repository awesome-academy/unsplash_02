package com.sun.unsplash_02.utils.task

import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.sun.unsplash_02.R
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.utils.sdk29AndUp
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask constructor(context: Context, private val listener: OnDownloadListener) :
    AsyncTask<Image, Int, String?>() {

    private var contextWeakReference: WeakReference<Context>? = null
    private var exception: Exception? = null

    init {
        contextWeakReference = WeakReference(context)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener.onStart()
    }

    override fun doInBackground(vararg params: Image): String? {
        contextWeakReference?.get()?.let { context ->
            var count: Int
            val url = URL(params[0].urls.full)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val lengthOfFile = connection.contentLength
            val input = BufferedInputStream(url.openStream())
            val data = ByteArray(1024)
            var total: Long = 0
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, params[0].id)
                put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
                }
            }
            var imageUri = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val resolver = context.contentResolver
            try {
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw IOException(context.getString(R.string.failed_create_media_record))
                resolver.openOutputStream(imageUri)?.use { output ->
                    while (input.read(data).also { count = it } != -1) {
                        total += count
                        publishProgress(((total * 100) / lengthOfFile).toInt())
                        output.write(data, 0, count)
                    }
                    output.flush()
                    input.close()
                }
                return imageUri.toString()
            } catch (e: IOException) {
                exception = e
                imageUri?.let { orphanUri ->
                    resolver.delete(orphanUri, null, null)
                }
            }
        }
        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let {
            listener.onProgressUpdate(it)
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        result?.let {
            listener.onComplete(it)
        } ?: run { listener.onError(exception) }
    }

    companion object {
        private const val MIME_TYPE = "image/jpeg"
    }
}
