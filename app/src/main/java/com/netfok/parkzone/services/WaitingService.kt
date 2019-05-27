package com.netfok.parkzone.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.netfok.parkzone.R
import com.netfok.parkzone.model.Location
import com.netfok.parkzone.model.ParkingZone
import com.netfok.parkzone.repository.ParkingRepository
import com.netfok.parkzone.ui.MapsActivity
import org.koin.android.ext.android.inject
import java.util.*

class WaitingService : Service() {
    private val parkingRepository: ParkingRepository by inject()
    private var startTime: Long? = null
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val pendingIntent: PendingIntent by lazy {
        Intent(this, MapsActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }
    }

    private val timer = Timer().apply {
        this.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val startTime = startTime ?: return
                val notification = buildNotification(
                    "In parking ${inParkingZone?.name}",
                    "Time: ${getHourWithMinutes(System.currentTimeMillis() - startTime)}"
                )
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }, 1000 * 60 * 3, 1000 * 60)
    }
    private var inParkingZone: ParkingZone? = null
        set(value) {
            if (field == value) return
            field?.let {
                val startTime = startTime ?: return@let
                val currentTime = System.currentTimeMillis()
                val parkingTime = currentTime - startTime
                val historyId = parkingRepository.saveHistory(it, parkingTime)
                val notification =
                    buildNotification("Was parking ${it.name}", "Time: ${getHourWithMinutes(parkingTime)}")
                notificationManager.notify(historyId, notification)
                this.startTime = null
            }
            if (value == null) {
                val notification = buildNotification("Not in parking", "")
                notificationManager.notify(NOTIFICATION_ID, notification)
            } else {
                startTime = System.currentTimeMillis()
            }
            field = value
        }

    private val waitingBinder by lazy { WaitingBinder() }

    inner class WaitingBinder : Binder() {
        fun getService() = this@WaitingService
    }

    override fun onBind(intent: Intent?) = waitingBinder

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val notification = buildNotification("Not in parking", "")
            startForeground(NOTIFICATION_ID, notification)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun buildNotification(title: String, text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    fun onLocationChanged(location: Location?) {
        val latlng = location?.let { LatLng(it.lat, it.lon) }
        inParkingZone = parkingRepository.parkingZones.find {
            PolyUtil.containsLocation(latlng, it.points.map { LatLng(it.lat, it.lon) }, false)
        }
    }

    fun getHourWithMinutes(time: Long): String {
        val seconds = time / 1000
        val minutes = seconds / 60 % 60
        val hours = seconds / 60 / 60
        return "$hours:${minutes / 10}${minutes % 10}"
    }


    companion object {
        const val CHANNEL_ID = "channel_01"
        const val NOTIFICATION_ID = 87654321
    }
}