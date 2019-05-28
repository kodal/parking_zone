package com.netfok.parkingzone

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}