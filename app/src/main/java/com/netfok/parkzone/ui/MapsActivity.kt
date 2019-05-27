package com.netfok.parkzone.ui

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.netfok.parkzone.R
import com.netfok.parkzone.model.Location
import com.netfok.parkzone.services.LocationService
import com.netfok.parkzone.ui.history.HistoryActivity
import com.netfok.parkzone.ui.list.ParkingZonesActivity
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.android.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private val viewModel: MapsViewModel by viewModel()

    private var locationMarker: Marker? = null
    private var parkingPolygons = emptyList<Polygon?>()

    private var locationService: LocationService? = null
        set(value) {
            field?.location?.removeObservers(this)
            value?.location?.observe(this, Observer(this::onLocationChanged))
            if (value == null && viewModel.locationEnabled.value == true) viewModel.switchLocationEnabled()
            if (value == null) locationMarker?.isVisible = false
            field = value
        }
    private val locationConnector =object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            locationService = null
        }
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            locationService = (service as? LocationService.LocationBinder)?.getService()
        }
    }

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
            }

            val showLocationDrawable = if (it) R.drawable.ic_location_on else R.drawable.ic_location_off
            val showLocationText = if (it) R.string.location_on else R.string.location_off

            show_location.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, showLocationDrawable), null, null, null
            )
            show_location.text = getString(showLocationText)
            if (it && isGranted()){
                ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java))
                bindService(Intent(this, LocationService::class.java), locationConnector, Context.BIND_AUTO_CREATE)
            } else {
                if (locationService != null) unbindService(locationConnector)
                locationService = null
                stopService(Intent(this, LocationService::class.java))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_history -> startActivity(Intent(this, HistoryActivity::class.java))
            R.id.menu_parking_zones -> startActivityForResult(Intent(this, ParkingZonesActivity::class.java), LIST_REQUEST)
        }
        return true
    }

    private fun isGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        locationMarker = map?.addMarker(MarkerOptions().title("Location").position(LatLng(42.877071, 74.603556)))?.apply {
            this.isVisible = false
        }
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(locationMarker?.position, 12f))
        viewModel.updateLocationEnabled()
        viewModel.parkingZones.observe(this, Observer {
            parkingPolygons = it.map { parkingZone ->
                map?.addPolygon(PolygonOptions()
                        .clickable(true)
                        .fillColor(ContextCompat.getColor(this, R.color.transparentRed))
                        .strokeColor(ContextCompat.getColor(this, R.color.red))
                        .addAll(parkingZone.points.map { l -> LatLng(l.lat, l.lon) })
                )?.apply {
                    tag = parkingZone.id
                }
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

    private fun onLocationChanged(location: Location?){
        val latlng = location?.let { LatLng(it.lat, it.lon) }
        locationMarker?.isVisible = location != null
        locationMarker?.position = latlng
        val inPolygon = parkingPolygons.find { PolyUtil.containsLocation(latlng, it?.points, false) }
        val inParkingZone = (inPolygon?.tag as? Int)?.let { viewModel.getParkingZone(it) }
        bottom_info.isVisible = inParkingZone != null
        bottom_name.text = inParkingZone?.name
        bottom_description.text = inParkingZone?.description
        bottom_image.setImageURI(inParkingZone?.image)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST &&
                        grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) viewModel.switchLocationEnabled()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LIST_REQUEST && resultCode == Activity.RESULT_OK){
            val selectedParkingZoneId = data?.getIntExtra(ParkingZonesActivity.PARKING_ZONE_ID, 0) ?: return
            val builder = LatLngBounds.builder()
            viewModel.getParkingZone(selectedParkingZoneId)?.points?.forEach {
                builder.include(LatLng(it.lat, it.lon))
            }
            val latLngBounds = builder.build()
            map?.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0))
        }
    }

    companion object {
        const val LOCATION_REQUEST = 101
        const val LIST_REQUEST = 23
    }
}
