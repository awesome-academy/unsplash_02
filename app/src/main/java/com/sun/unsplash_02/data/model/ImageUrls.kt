package com.sun.unsplash_02.data.model

data class ImageUrls(
    val full: String = "",
    val small: String = ""
)

object ImageUrlsEntry {
    const val OBJECT = "urls"
    const val REGULAR = "regular"
    const val SMALL = "small"
}
