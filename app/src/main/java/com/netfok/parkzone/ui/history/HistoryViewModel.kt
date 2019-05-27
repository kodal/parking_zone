package com.netfok.parkzone.ui.history

import androidx.lifecycle.ViewModel
import com.netfok.parkzone.repository.ParkingRepository

class HistoryViewModel(val repo: ParkingRepository) : ViewModel(){
    val histories = repo.getHistories()
    fun delete(historyId: Int) = repo.delete(historyId)
}