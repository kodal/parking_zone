package com.netfok.parkzone.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.netfok.parkzone.R
import com.netfok.parkzone.ui.MapsActivity

class LocationService : Service() {
    private val waitingBinder by lazy { LocationBinder() }

    private val _location = MutableLiveData<com.netfok.parkzone.model.Location>()
    val location: LiveData<com.netfok.parkzone.model.Location> = _location

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            _location.value = location?.let { com.netfok.parkzone.model.Location(it.latitude, it.longitude) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onBind(intent: Intent?) = waitingBinder

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val pendingIntent: PendingIntent =
                Intent(this, MapsActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent, 0)
                }

            val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.location_on))
                .setContentText(getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .build()
            startForeground(NOTIFICATION_ID, notification)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            stopForeground(true)
            return
        }
        _location.value = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).run {
            com.netfok.parkzone.model.Location(latitude, longitude)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, locationListener)


    }


    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        }
    }

    inner class LocationBinder : Binder() {
        fun getService() = this@LocationService
    }

    companion object {
        const val CHANNEL_ID = "channel_01"
        const val NOTIFICATION_ID = 12345678
    }
}