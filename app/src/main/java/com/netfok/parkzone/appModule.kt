package com.netfok.parkzone

import androidx.room.Room
import com.netfok.parkzone.database.AppDatabase
import com.netfok.parkzone.repository.ParkingRepository
import com.netfok.parkzone.ui.maps.MapsViewModel
import com.netfok.parkzone.ui.history.HistoryViewModel
import com.netfok.parkzone.ui.list.ParkingZonesViewModel
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