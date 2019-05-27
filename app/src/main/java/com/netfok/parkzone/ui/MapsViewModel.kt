package com.netfok.parkzone.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.netfok.parkzone.repository.ParkingRepository
import com.netfok.parkzone.model.ParkingZone

class MapsViewModel(
    private val parkingRepository: ParkingRepository
): ViewModel() {
    private val _locationEnabled = MutableLiveData<Boolean>().apply { value = false }
    val locationEnabled: LiveData<Boolean> = _locationEnabled
    val parkingZones = parkingRepository.parkingZones

    fun switchLocationEnabled(){
        _locationEnabled.value = _locationEnabled.value?.not()
    }
    fun updateLocationEnabled() {
        _locationEnabled.value = _locationEnabled.value
    }

    fun getParkingZone(id: Int): ParkingZone? = parkingZones.value?.find { it.id == id }
}