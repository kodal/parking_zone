package com.netfok.parkzone

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MapsViewModel() }
}