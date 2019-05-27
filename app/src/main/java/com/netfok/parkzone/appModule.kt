package com.netfok.parkzone

import com.netfok.parkzone.repository.ParkingRepository
import com.netfok.parkzone.ui.MapsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ParkingRepository() }

    viewModel { MapsViewModel(get()) }
}