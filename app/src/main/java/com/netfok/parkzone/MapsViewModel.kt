package com.netfok.parkzone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsViewModel: ViewModel() {
    private val _locationEnabled = MutableLiveData<Boolean>().apply { value = false }
    val locationEnabled: LiveData<Boolean> = _locationEnabled

    fun switchLocationEnabled(){
        _locationEnabled.value = _locationEnabled.value?.not()
    }
    fun updateLocationEnabled() {
        _locationEnabled.value = _locationEnabled.value
    }
}