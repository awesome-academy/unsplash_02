package com.sun.unsplash_02.screen.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.utils.LoadImageTask
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listPhotos = mutableListOf<Image?>()
    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ITEM) {
            return PhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_photo,
                    parent,
                    false
                ), itemClickListener
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

    fun getListPhotos() = listPhotos

    fun clear() {
        val oldListSize = itemCount
        getListPhotos().clear()
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

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    class PhotoViewHolder(view: View, private val itemClickListener: ItemClickListener?) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private var listener: ItemClickListener? = null
        private var imageData: Image? = null

        fun bind(image: Image) =
            with(itemView) {
                image.let {
                    LoadImageTask(imageUnsplash).execute(image.urls.small)
                    setOnClickListener(this@PhotoViewHolder)
                    listener = itemClickListener
                    imageData = it
                }
            }

        override fun onClick(v: View?) {
            listener?.onItemClick(imageData)
        }
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface ItemClickListener {
        fun onItemClick(image: Image?)
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}
