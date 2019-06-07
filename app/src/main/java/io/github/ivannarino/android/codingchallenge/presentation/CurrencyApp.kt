package io.github.ivannarino.android.codingchallenge.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication

class CurrencyApp : MultiDexApplication() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appContext: Context

        const val FIXER_IO_ACCESS_KEY = "bef61bd2358b869ded2e35e2ba3f82f8"
        const val FIXER_IO_BASE_URL = "http://data.fixer.io/api/"
        const val FIXER_DEFAULT_BASE_CURRENCY = "EUR"
        const val DEFAULT_CURRENCY = "USD"
        val CONVERT_CURRENCIES = listOf("GBP", "EUR", "JPY", "BRL")

        // Chart constants
        const val BAR_WIDTH = 0.8f
        const val Y_ANIMATION_DURATION = 2500
        const val X_AXIS_GRANULARITY = 1f
        const val DATASET_NAME = "Currencies"
        const val CHART_VALUE_FORMAT = "###,##0.00"

        // Room
        const val DB_NAME = "currency-db"

        const val DEBOUNCE_TIMEOUT = 600L
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }
}