package io.github.ivannarino.android.codingchallenge.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.ivannarino.android.codingchallenge.data.CurrencyDb
import io.github.ivannarino.android.codingchallenge.data.CurrencyWs
import io.github.ivannarino.android.codingchallenge.domain.CurrencyRepository
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.DEFAULT_CURRENCY

class ConversionViewModel : ViewModel() {

    private val currencyRepository: CurrencyRepository = CurrencyRepository(CurrencyDb(), CurrencyWs())

    private val conversionStateData = MutableLiveData<Conversion>()

    fun getConversionStateData(): LiveData<Conversion> = conversionStateData

    fun convert(value: Int) {
        val currencyConversions = currencyRepository.getCurrencyConversions(value.toBigDecimal(), DEFAULT_CURRENCY, listOf("GBP", "EUR", "JPY", "BRL"))
        conversionStateData.value = currencyConversions
    }
}
