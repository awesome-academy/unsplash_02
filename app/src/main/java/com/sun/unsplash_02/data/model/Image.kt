package com.sun.unsplash_02.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val id: String = "",
    val urls: ImageUrls
) : Parcelable

object ImageEntry {
    const val RESULTS = "results"
    const val IMAGE = "images"
    const val ID = "id"
}
