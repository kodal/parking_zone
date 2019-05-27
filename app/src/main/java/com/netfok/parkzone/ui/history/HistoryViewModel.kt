package com.netfok.parkzone.ui.history

import androidx.lifecycle.ViewModel
import com.netfok.parkzone.repository.ParkingRepository

class HistoryViewModel(repo: ParkingRepository) : ViewModel(){
    val histories = repo.getHistories()
}