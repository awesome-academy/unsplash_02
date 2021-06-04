package com.sun.unsplash_02.screen.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.utils.task.LoadImageTask
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter(
    private val onRecyclerItemClickListener: ((Image) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listPhotos = mutableListOf<Image?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            return PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_photo,
                    parent,
                    false
                ), onRecyclerItemClickListener
            )
        } else {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = listPhotos[position]
        if (holder is PhotoViewHolder) {
            photo?.let {
                holder.bind(it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listPhotos[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount() = listPhotos.size ?: 0

    fun setListPhotos(images: MutableList<Image?>) {
        listPhotos.addAll(images)
        notifyDataSetChanged()
    }

    fun clear() {
        val oldListSize = itemCount
        listPhotos.clear()
        notifyItemRangeRemoved(0, oldListSize)
    }

    fun startLoadMore() {
        listPhotos.add(null)
        notifyItemInserted(itemCount - 1)
    }

    fun stopLoadMore() {
        listPhotos.removeAt(itemCount - 1)
        notifyItemRemoved(itemCount)
    }

    class PhotoViewHolder(view: View, private val onRecyclerItemClickListener: ((Image) -> Unit)) :
        RecyclerView.ViewHolder(view) {

        private var imageData: Image? = null

        init {
            itemView.setOnClickListener {
                imageData?.let {
                    onRecyclerItemClickListener(it)
                }
            }
        }

        fun bind(image: Image) =
            with(itemView) {
                LoadImageTask(imageUnsplash).execute(image.urls.small)
                imageData = image
            }
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}
