package io.github.ivannarino.android.codingchallenge.presentation

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.ivannarino.android.codingchallenge.data.CurrencyDb
import io.github.ivannarino.android.codingchallenge.data.CurrencyWs
import io.github.ivannarino.android.codingchallenge.domain.CurrencyRepository
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.CONVERT_CURRENCIES
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.DEFAULT_CURRENCY
import io.github.ivannarino.android.codingchallenge.presentation.util.hideSoftKeyboard

class ConversionViewModel : ViewModel() {

    private val currencyRepository: CurrencyRepository = CurrencyRepository(CurrencyDb(), CurrencyWs())

    private val conversionStateData = MutableLiveData<Conversion>()

    fun getConversionStateData(): LiveData<Conversion> = conversionStateData

    fun convert(context: Context, value: String) {
        (context as Activity).hideSoftKeyboard()

        value.toBigIntegerOrNull()?.let {
            val currencyConversions = currencyRepository.getCurrencyConversions(value.toBigDecimal(), DEFAULT_CURRENCY, CONVERT_CURRENCIES)
            conversionStateData.value = currencyConversions
        }

    }
}
