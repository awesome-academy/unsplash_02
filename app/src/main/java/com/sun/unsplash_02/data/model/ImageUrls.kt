package com.sun.unsplash_02.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUrls(
    val full: String = "",
    val small: String = ""
) : Parcelable

object ImageUrlsEntry {
    const val OBJECT = "urls"
    const val REGULAR = "regular"
    const val SMALL = "small"
}
