package com.netfok.parkingzone.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.netfok.parkingzone.database.AppDatabase
import com.netfok.parkingzone.model.History
import com.netfok.parkingzone.model.Location
import com.netfok.parkingzone.model.ParkingZone

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
            ),
            false
        ),
        ParkingZone(
            2, "Bishkek Park", "Market",
            "https://media-cdn.tripadvisor.com/media/photo-s/09/03/52/16/caption.jpg",
            listOf(
                Location(42.874481, 74.590190),
                Location(42.874822, 74.590248),
                Location(42.874820, 74.590511),
                Location(42.874553, 74.590473),
                Location(42.874560, 74.590396),
                Location(42.874479, 74.590394)
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

    fun delete(historyId: Int) = appDatabase.historyDao().delete(historyId)
}