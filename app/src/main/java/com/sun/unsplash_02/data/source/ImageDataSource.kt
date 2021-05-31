package com.sun.unsplash_02.data.source

import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.source.remote.OnFetchDataJsonListener

interface ImageDataSource {

    interface Local

    interface Remote {

        fun getListCollections(listener: OnFetchDataJsonListener<MutableList<Collection>>)

        fun getListImages(listener: OnFetchDataJsonListener<MutableList<Image?>>, page: Int)

        fun getListImageByCollectionId(
            listener: OnFetchDataJsonListener<MutableList<Image?>>,
            collectionId: String,
            page: Int
        )

        fun searchImage(
            listener: OnFetchDataJsonListener<MutableList<Image?>>,
            query: String,
            page: Int
        )
    }
}
