package com.netfok.parkzone.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.netfok.parkzone.R
import kotlinx.android.synthetic.main.activity_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {
    private val viewModel: HistoryViewModel by viewModel()
    private val historyAdapter by lazy { HistoryAdapter(viewModel::delete) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        history_recycler.adapter = historyAdapter
        viewModel.histories.observe(this, Observer(historyAdapter::submitList))
    }
}