package io.github.ivannarino.android.codingchallenge.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import io.github.ivannarino.android.codingchallenge.R
import io.github.ivannarino.android.codingchallenge.databinding.ConversionFragmentBinding
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.X_AXIS_GRANULARITY
import kotlinx.android.synthetic.main.conversion_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversionFragment : Fragment() {

    private val conversationViewModel: ConversionViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: ConversionFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.conversion_fragment, container, false)

        binding.lifecycleOwner = this
        binding.vm = conversationViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chart.setStyle()
    }
}

fun HorizontalBarChart.setStyle() {
    xAxis.position = XAxisPosition.BOTTOM
    xAxis.setDrawAxisLine(false)
    xAxis.setDrawGridLines(false)
    xAxis.granularity = X_AXIS_GRANULARITY
    xAxis.setAvoidFirstLastClipping(false)

    axisLeft.isEnabled = false
    axisRight.isEnabled = false
    axisLeft.axisMinimum = 0f
    axisRight.axisMinimum = 0f

    legend.isEnabled = false
    description.isEnabled = false

    setFitBars(true)
    setTouchEnabled(false)
    setNoDataText(null)
}
