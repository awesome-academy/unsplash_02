package com.sun.unsplash_02.data.source

import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.remote.OnFetchDataJsonListener

interface ImageDataSource {

    interface Local

    interface Remote {
        fun getListCollections(listener: OnFetchDataJsonListener<MutableList<Collection>>)
        fun getListImages(listener: OnFetchDataJsonListener<MutableList<Image>>, page: Int)
    }
}
