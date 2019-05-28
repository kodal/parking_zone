package com.netfok.parkingzone.model

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon"])
data class Location(
    val lat: Double,
    val lon: Double
)