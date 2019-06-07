package io.github.ivannarino.android.codingchallenge.presentation

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.ivannarino.android.codingchallenge.R
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.BAR_WIDTH
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.CHART_VALUE_FORMAT
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.DATASET_NAME
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.Y_ANIMATION_DURATION
import java.text.DecimalFormat
import kotlin.collections.ArrayList as ArrayList1

@BindingAdapter("chartData")
fun HorizontalBarChart.setChartData(conversionData: Conversion?) {
    conversionData?.let {
        xAxis.valueFormatter = CurrencyFormatter(conversionData)
        xAxis.labelCount = conversionData.conversions.size

        val values = conversionData.conversions.mapIndexed { index, amount ->
            BarEntry(index.toFloat(), amount.value.toFloat())
        }

        if (data != null && data.dataSets.isNotEmpty()) {
            (data.getDataSetByLabel(DATASET_NAME, false) as? BarDataSet)?.values = values
            data.notifyDataChanged()
            notifyDataSetChanged()
        } else {
            val barDataSet = BarDataSet(values, DATASET_NAME)
            barDataSet.color = ContextCompat.getColor(context, R.color.colorPrimary)
            barDataSet.valueFormatter = CurrencyFormatter(conversionData)
            barDataSet.setDrawIcons(false)

            val barData = BarData(listOf(barDataSet))
            barData.barWidth = BAR_WIDTH

            data = barData
        }
        animateY(Y_ANIMATION_DURATION)
    }
}


class CurrencyFormatter(private val conversion: Conversion) : ValueFormatter() {
    private val format = DecimalFormat(CHART_VALUE_FORMAT)

    override fun getBarLabel(barEntry: BarEntry?): String {
        return format.format(barEntry?.y)
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return conversion.conversions.getOrNull(value.toInt())?.currency ?: value.toString()
    }
}