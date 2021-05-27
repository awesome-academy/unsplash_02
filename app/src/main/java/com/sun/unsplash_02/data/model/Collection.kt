package com.sun.unsplash_02.data.model

data class Collection (
    val id: String = "",
    val title: String = "",
)

object CollectionEntry {
    const val COLLECTION = "COLLECTION"
    const val ID = "id"
    const val TITLE = "title"
}
