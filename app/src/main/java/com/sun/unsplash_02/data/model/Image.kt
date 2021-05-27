package com.sun.unsplash_02.data.model

data class Image(
    val id: String = "",
    val urls: ImageUrls
)

object ImageEntry {
    const val IMAGE = "images"
    const val ID = "id"
}
