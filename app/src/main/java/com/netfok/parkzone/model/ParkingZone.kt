package com.netfok.parkzone.model

data class ParkingZone(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val points: List<Location>
)