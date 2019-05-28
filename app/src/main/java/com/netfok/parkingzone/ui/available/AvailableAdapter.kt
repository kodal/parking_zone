package com.netfok.parkingzone.ui.available

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netfok.parkingzone.R
import com.netfok.parkingzone.model.ParkingZone
import kotlinx.android.synthetic.main.item_image_title_description.view.*

class AvailableAdapter : ListAdapter<ParkingZone, AvailableAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_title_description, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(parkingZone: ParkingZone?) = itemView.apply {
            item_itd_name.text = parkingZone?.name
            item_itd_description.text = parkingZone?.description
            item_itd_image.setImageURI(parkingZone?.image)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ParkingZone>() {
            override fun areItemsTheSame(oldItem: ParkingZone, newItem: ParkingZone): Boolean {
                Log.e("tag", "$oldItem $newItem")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ParkingZone, newItem: ParkingZone): Boolean {
                return oldItem == newItem
            }
        }
    }
}