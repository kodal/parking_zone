package com.netfok.parkingzone.ui.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.netfok.parkingzone.R
import kotlinx.android.synthetic.main.activity_parking_zones.*
import org.koin.android.viewmodel.ext.android.viewModel

class ParkingZonesActivity : AppCompatActivity() {
    private val parkingZonesAdapter = ParkingZonesAdapter(this::onSelect)
    private val viewModel: ParkingZonesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_zones)
        parking_zones_recycler.adapter = parkingZonesAdapter
        viewModel.parkingZones.observe(this, Observer(parkingZonesAdapter::submitList))
    }

    private fun onSelect(parkingZoneId: Int) {
        setResult(Activity.RESULT_OK, Intent().putExtra(PARKING_ZONE_ID, parkingZoneId))
        finish()
    }

    companion object {
        const val PARKING_ZONE_ID = "parking_zone_id"
    }
}