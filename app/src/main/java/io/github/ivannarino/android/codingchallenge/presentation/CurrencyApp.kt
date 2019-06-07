package io.github.ivannarino.android.codingchallenge.presentation

import android.app.Application

class CurrencyApp : Application() {

    companion object {
        const val DEFAULT_CURRENCY = "USD"
        val CONVERT_CURRENCIES = listOf("GBP", "EUR", "JPY", "BRL")

        // Chart constants
        const val BAR_WIDTH = 0.8f
        const val Y_ANIMATION_DURATION = 2500
        const val X_AXIS_GRANULARITY = 1f
        const val DATASET_NAME = "Currencies"
        const val CHART_VALUE_FORMAT = "###,##0.00"

    }
}