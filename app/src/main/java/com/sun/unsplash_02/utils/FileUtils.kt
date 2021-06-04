package com.sun.unsplash_02.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.sun.unsplash_02.R
import java.io.IOException

object FileUtils {

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context, bitmap: Bitmap, displayName: String
    ): Uri {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            }
        }
        val resolver = context.contentResolver
        var uri: Uri? = null
        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: throw IOException(context.getString(R.string.failed_create_media_record))
            resolver.openOutputStream(uri)?.use {
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 95, it))
                    throw IOException(context.getString(R.string.failed_save_bitmap))
            } ?: throw IOException(context.getString(R.string.failed_open_output_stream))
            return uri
        } catch (e: IOException) {
            uri?.let { orphanUri ->
                resolver.delete(orphanUri, null, null)
            }
            throw e
        }
    }
}
