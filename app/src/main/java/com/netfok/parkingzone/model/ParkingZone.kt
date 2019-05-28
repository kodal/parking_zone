package com.netfok.parkingzone.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParkingZone(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val points: List<Location> = emptyList(),
    val isAvailable: Boolean = true
)