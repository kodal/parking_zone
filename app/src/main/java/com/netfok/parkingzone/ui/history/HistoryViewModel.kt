package com.netfok.parkingzone.ui.history

import androidx.lifecycle.ViewModel
import com.netfok.parkingzone.repository.ParkingRepository

class HistoryViewModel(val repo: ParkingRepository) : ViewModel(){
    val histories = repo.getHistories()
    fun delete(historyId: Int) = repo.delete(historyId)
}