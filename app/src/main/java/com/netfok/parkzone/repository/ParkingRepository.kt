package com.netfok.parkzone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.netfok.parkzone.database.AppDatabase
import com.netfok.parkzone.model.History
import com.netfok.parkzone.model.Location
import com.netfok.parkzone.model.ParkingZone

class ParkingRepository(
    private val appDatabase: AppDatabase
) {
    val parkingZones = listOf(
        ParkingZone(
            2, "Bishkek", "Square",
            "https://upload.wikimedia.org/wikipedia/commons/7/7e/" +
                    "Ala-too_Square_in_Bishkek%2C_Kyrgyzstan%2C_2007-09-11_%28color-corrected%29.jpg",
            listOf(
                Location(42.877011, 74.603083),
                Location(42.876977, 74.604153),
                Location(42.876313, 74.604128),
                Location(42.876392, 74.603065)
            )
        )
    )
    val parkingZonesLive: LiveData<List<ParkingZone>> = MutableLiveData<List<ParkingZone>>().apply {
        value = parkingZones
    }

    fun saveHistory(parkingZone: ParkingZone, parkingTime: Long) {
        appDatabase.historyDao().insert(History(parkingZone = parkingZone, time = parkingTime))
    }

    fun getHistories(): LiveData<List<History>> {
        return appDatabase.historyDao().getAll()
    }
}