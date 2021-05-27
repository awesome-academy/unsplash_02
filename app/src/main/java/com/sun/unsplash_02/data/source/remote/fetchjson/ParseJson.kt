package com.sun.unsplash_02.data.source.remote.fetchjson

import com.sun.unsplash_02.data.model.*
import com.sun.unsplash_02.data.model.Collection
import org.json.JSONObject

object ParseJson {

    private fun urlsImageParseJson(jsonObject: JSONObject): ImageUrls = ImageUrls(
        jsonObject.getString(ImageUrlsEntry.REGULAR),
        jsonObject.getString(ImageUrlsEntry.THUMB)
    )

    fun imageParseJson(jsonObject: JSONObject): Image {
        val imageUrls = urlsImageParseJson(jsonObject.getJSONObject(ImageUrlsEntry.OBJECT))
        return Image(
            jsonObject.getString(ImageEntry.ID),
            imageUrls
        )
    }

    fun collectionParseJson(jsonObject: JSONObject): Collection = Collection(
        jsonObject.getString(CollectionEntry.ID),
        jsonObject.getString(CollectionEntry.TITLE)
    )
}
