package com.sun.unsplash_02.utils

import com.sun.unsplash_02.BuildConfig

object Constants {
    private const val BASE_URL = "https://api.unsplash.com"
    private const val COLLECTIONS = "/collections"
    private const val DEFAULT_PAGE_COLLECTION = "8"
    private const val SEARCH = "/search"
    const val API_KEY = "?client_id=${BuildConfig.API_KEY}"
    const val PHOTOS = "/photos"
    const val QUERY = "&query="
    const val PAGE = "&page="
    const val URL_COLLECTIONS = "$BASE_URL$COLLECTIONS$API_KEY$PAGE$DEFAULT_PAGE_COLLECTION"
    const val URL_PHOTOS = "$BASE_URL$PHOTOS$API_KEY$PAGE"
    const val URL_COLLECTION_PHOTO = "$BASE_URL$COLLECTIONS/"
    const val URL_SEARCH_PHOTOS = "$BASE_URL$SEARCH$PHOTOS$API_KEY"
    const val VISIBLE_THRESHOLD = 3
}
