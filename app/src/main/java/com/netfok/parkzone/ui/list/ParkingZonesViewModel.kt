package com.netfok.parkzone.ui.list

import androidx.lifecycle.ViewModel
import com.netfok.parkzone.repository.ParkingRepository

class ParkingZonesViewModel(repo: ParkingRepository) : ViewModel(){
    val parkingZones = repo.parkingZonesLive
}