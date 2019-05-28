package com.netfok.parkingzone.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.netfok.parkingzone.model.Location
import com.netfok.parkingzone.model.ParkingZone
import com.netfok.parkingzone.repository.ParkingRepository

class MapsViewModel(
    private val parkingRepository: ParkingRepository
) : ViewModel() {
    private val _locationEnabled = MutableLiveData<Boolean>().apply { value = false }
    val locationEnabled: LiveData<Boolean> = _locationEnabled
    val parkingZones = parkingRepository.parkingZonesLive
    private val _location = MutableLiveData<Location?>()
    private val _zonesByNearest = MutableLiveData<List<ParkingZone>>()
    val zonesByNearest: LiveData<List<ParkingZone>> = _zonesByNearest
    val zonesAvailable = Transformations.map(zonesByNearest){
        it.filter { it.isAvailable }
    }

    init {
        _location.observeForever { updateNearestZones(parkingZones.value, it) }
        parkingZones.observeForever { updateNearestZones(it, _location.value) }
    }

    fun switchLocationEnabled() {
        _locationEnabled.value = _locationEnabled.value?.not()
    }

    fun updateLocationEnabled() {
        _locationEnabled.value = _locationEnabled.value
    }

    fun getParkingZone(id: Int): ParkingZone? = parkingZones.value?.find { it.id == id }

    fun change(location: Location?) {
        _location.value = location
    }

    private fun updateNearestZones(parkingZones: List<ParkingZone>?, location: Location?) {
        if (location == null || parkingZones.isNullOrEmpty()) {
            _zonesByNearest.value = emptyList()
            return
        }
        val zonesByNearest = parkingZones.sortedBy {
            val boundsBuilder = LatLngBounds.builder()
            it.points.forEach {
                boundsBuilder.include(LatLng(it.lat, it.lon))
            }
            val zoneCenter = boundsBuilder.build().center
            val centerLocation = android.location.Location("").apply {
                latitude = zoneCenter.latitude
                longitude = zoneCenter.longitude
            }
            val androidLocation = android.location.Location("")
            androidLocation.apply {
                latitude = location.lat
                longitude = location.lon
            }
            androidLocation.distanceTo(centerLocation)
        }
        _zonesByNearest.value = zonesByNearest
    }
}