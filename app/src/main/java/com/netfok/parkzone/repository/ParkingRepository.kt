package com.netfok.parkzone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.netfok.parkzone.model.Location
import com.netfok.parkzone.model.ParkingZone

class ParkingRepository {
    val parkingZones: LiveData<List<ParkingZone>> = MutableLiveData<List<ParkingZone>>().apply {
        value = listOf(
            ParkingZone(
                1,
                "name",
                "description",
                "",
                listOf(
                    Location(42.831551, 74.616212),
                    Location(42.830944, 74.617049),
                    Location(42.829969, 74.615879),
                    Location(42.830598, 74.614978)
                )
            ),
            ParkingZone(
                2, "Bishkek", "Square", "",
                listOf(
                    Location(42.877011, 74.603083),
                    Location(42.876977, 74.604153),
                    Location(42.876313, 74.604128),
                    Location(42.876392, 74.603065)
                )
            )
        )
    }
}