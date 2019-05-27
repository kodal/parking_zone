package com.netfok.parkzone.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netfok.parkzone.R
import com.netfok.parkzone.model.ParkingZone
import kotlinx.android.synthetic.main.item_parking_zone.view.*

class ParkingZonesAdapter(
    val onSelect: (Int) -> Unit
) : ListAdapter<ParkingZone, ParkingZonesAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_parking_zone, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val parkingZoneId = getItem(adapterPosition)?.id ?:return@setOnClickListener
                onSelect(parkingZoneId)
            }
        }

        fun bind(parkingZone: ParkingZone?) = itemView.apply {
            item_parking_zone_name.text = parkingZone?.name
            item_parking_zone_description.text = parkingZone?.description
            item_parking_zone_image.setImageURI(parkingZone?.image)
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