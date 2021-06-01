package com.sun.unsplash_02.screen.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.unsplash_02.R
import kotlinx.android.synthetic.main.item_search_history.view.*

class HistorySearchAdapter(
    private val onRecyclerItemClickListener: ((String) -> Unit)
) : RecyclerView.Adapter<HistorySearchAdapter.HistorySearchViewHolder>() {

    private var listHistorySearch = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorySearchViewHolder =
        HistorySearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_history,
                parent, false
            ), onRecyclerItemClickListener
        )

    override fun onBindViewHolder(holder: HistorySearchViewHolder, position: Int) =
        holder.bind(listHistorySearch[position])

    override fun getItemCount() = listHistorySearch.size

    fun setListHistory(listHistory: MutableList<String>) {
        listHistorySearch = listHistory
        notifyDataSetChanged()
    }

    class HistorySearchViewHolder(
        view: View,
        private val onRecyclerItemClickListener: ((String) -> Unit)
    ) :
        RecyclerView.ViewHolder(view) {

        private var historyData: String? = null

        init {
            itemView.setOnClickListener { _ ->
                historyData?.let {
                    onRecyclerItemClickListener(it)
                }
            }
        }

        fun bind(history: String) = with(itemView) {
            textHistory.text = history
            historyData = history
        }
    }
}
