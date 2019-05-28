package com.netfok.parkingzone.ui.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.netfok.parkingzone.R
import com.netfok.parkingzone.ui.maps.MapsViewModel
import kotlinx.android.synthetic.main.fragment_available.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class AvailableFragment: BottomSheetDialogFragment() {
    private val nearestAdapter by lazy { AvailableAdapter() }
    private val mapsViewModel: MapsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_available, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nearest_recycler.adapter = nearestAdapter
        mapsViewModel.zonesAvailable.observe(viewLifecycleOwner, Observer(nearestAdapter::submitList))
    }


}