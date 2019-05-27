package com.netfok.parkzone.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["lat", "lon"])
data class Location(
    val lat: Double,
    val lon: Double
)