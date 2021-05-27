package com.sun.unsplash_02.data.source.remote

import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.CollectionEntry
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.model.ImageEntry
import com.sun.unsplash_02.data.source.ImageDataSource
import com.sun.unsplash_02.data.source.remote.fetchjson.GetJsonFromUrl
import com.sun.unsplash_02.utils.Constants

class ImageRemoteDataResource : ImageDataSource.Remote {

    override fun getListCollections(listener: OnFetchDataJsonListener<MutableList<Collection>>) {
        GetJsonFromUrl(
            listener = listener,
            keyEntity = CollectionEntry.COLLECTION
        ).execute(Constants.URL_COLLECTIONS)
    }

    override fun getListImages(listener: OnFetchDataJsonListener<MutableList<Image>>, page: Int) {
        GetJsonFromUrl(
            listener = listener,
            keyEntity = ImageEntry.IMAGE
        ).execute("${Constants.URL_PHOTOS}$page")
    }

    companion object {
        @Volatile
        private var instance: ImageRemoteDataResource? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ImageRemoteDataResource().also { instance = it }
        }
    }
}
