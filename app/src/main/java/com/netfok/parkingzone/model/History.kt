package com.netfok.parkingzone.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded(prefix = "parkingZone_")
    val parkingZone: ParkingZone,
    val time: Long
)