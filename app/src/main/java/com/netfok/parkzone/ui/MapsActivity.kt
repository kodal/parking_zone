package com.netfok.parkzone.ui

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.netfok.parkzone.R
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.android.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private val viewModel: MapsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        show_location.setOnClickListener {
            viewModel.switchLocationEnabled()
        }

        viewModel.locationEnabled.observe(this, Observer {
            if (it && !isGranted()){
                viewModel.switchLocationEnabled()
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_REQUEST
                )
            } else {
                map?.isMyLocationEnabled = it
                map?.uiSettings?.isMyLocationButtonEnabled = it
            }

            val showLocationDrawable = if (it) R.drawable.ic_location_on else R.drawable.ic_location_off
            val showLocationText = if (it) R.string.location_on else R.string.location_off

            show_location.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, showLocationDrawable), null, null, null
            )
            show_location.text = getString(showLocationText)
        })
    }

    private fun isGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewModel.updateLocationEnabled()
        viewModel.parkingZones.observe(this, Observer {
            it.forEach { parkingZone ->
                map?.addPolygon(PolygonOptions()
                        .clickable(true)
                        .fillColor(ContextCompat.getColor(this, R.color.transparentRed))
                        .strokeColor(ContextCompat.getColor(this, R.color.red))
                        .addAll(parkingZone.points.map { l -> LatLng(l.lat, l.lon) })
                )?.tag = parkingZone.id
            }
        })
        map?.setOnPolygonClickListener {
            val parkingZone = viewModel.getParkingZone(it.tag as? Int ?: return@setOnPolygonClickListener)
            AlertDialog.Builder(this)
                    .setTitle(parkingZone?.name)
                    .setMessage(parkingZone?.description)
                    .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, _: Int ->
                        dialog?.dismiss()
                    }
                    .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST &&
                        grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) viewModel.switchLocationEnabled()
    }

    companion object {
        const val LOCATION_REQUEST = 101
    }
}
