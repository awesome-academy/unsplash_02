package com.sun.unsplash_02.screen.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import com.sun.unsplash_02.data.model.Collection
import kotlinx.android.synthetic.main.item_collection.view.*

class CollectionAdapter(
    private val onRecyclerItemClickListener: (Collection) -> Unit
) :
    RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    private var rowSelected = -1
    private var listCollections = mutableListOf<Collection>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder =
        CollectionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_collection,
                parent,
                false
            ), onRecyclerItemClickListener
        )

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val collection = listCollections[position]
        holder.bind(collection, position)
    }

    override fun getItemCount() = listCollections.size

    fun setListCollections(collections: MutableList<Collection>) {
        listCollections = collections
        notifyDataSetChanged()
    }

    inner class CollectionViewHolder(
        view: View,
        private val onRecyclerItemClickListener: (Collection) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private var collectionData: Collection? = null

        init {
            itemView.setOnClickListener {
                collectionData?.let {
                    onRecyclerItemClickListener(it)
                    rowSelected = position
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(collection: Collection, position: Int) = with(itemView) {
            if (rowSelected == position) {
                linearCollection.setBackgroundResource(R.color.dark_gray)
            } else {
                linearCollection.setBackgroundResource(R.color.gray_100)
            }
            textCollectionName.text = collection.title
            collectionData = collection
        }
    }
}
