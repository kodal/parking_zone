package com.netfok.parkzone.ui.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netfok.parkzone.R
import com.netfok.parkzone.extensions.toHoursWithMinutes
import com.netfok.parkzone.model.History
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(
    val onDelete: (Int) -> Unit = {}
) : ListAdapter<History, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.remove.setOnClickListener { onDelete(getItem(adapterPosition).id) }
        }
        fun bind(history: History?) = itemView.apply {
            item_history_name.text = history?.parkingZone?.name
            item_history_description.text = history?.time?.toHoursWithMinutes()
            item_history_image.setImageURI(history?.parkingZone?.image)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                Log.e("tag", "$oldItem $newItem")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

        }
    }
}