package com.netfok.parkingzone.ui.list

import androidx.lifecycle.ViewModel
import com.netfok.parkingzone.repository.ParkingRepository

class ParkingZonesViewModel(repo: ParkingRepository) : ViewModel(){
    val parkingZones = repo.parkingZonesLive
}