package com.netfok.parkingzone

import androidx.room.Room
import com.netfok.parkingzone.database.AppDatabase
import com.netfok.parkingzone.repository.ParkingRepository
import com.netfok.parkingzone.ui.maps.MapsViewModel
import com.netfok.parkingzone.ui.history.HistoryViewModel
import com.netfok.parkingzone.ui.list.ParkingZonesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    single { ParkingRepository(get()) }


    viewModel { ParkingZonesViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}