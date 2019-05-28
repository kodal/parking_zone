package com.netfok.parkingzone.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.netfok.parkingzone.R
import com.netfok.parkingzone.ui.maps.MapsViewModel
import kotlinx.android.synthetic.main.fragment_parking_zones.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ParkingZonesFragment : BottomSheetDialogFragment() {
    var onSelect: (parkingZoneId: Int) -> Unit = {}
    private val parkingZonesAdapter = ParkingZonesAdapter(this::onZoneSelect)

    private val viewModel: MapsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_parking_zones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parking_zones_recycler.adapter = parkingZonesAdapter
        sort_by_nearest.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.sortNearest(isChecked)
        }
        viewModel.listParkingZones.observe(this, Observer(parkingZonesAdapter::submitList))
    }

    private fun onZoneSelect(parkingZoneId: Int){
        onSelect(parkingZoneId)
        dismiss()
    }
}