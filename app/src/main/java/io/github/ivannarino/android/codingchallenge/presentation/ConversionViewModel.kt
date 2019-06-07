package io.github.ivannarino.android.codingchallenge.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConversionViewModel : ViewModel() {

    private val currencyRepository: CurrencyRepository = CurrencyRepository(CurrencyDb(), CurrencyWs())

    private val conversionStateData = MutableLiveData<Conversion>()

    private val compositeDisposable = CompositeDisposable()

    fun getConversionStateData(): LiveData<Conversion> = conversionStateData

    fun convert(context: Context, value: String) {
        context.hideSoftKeyboard()

        value.toBigIntegerOrNull()?.let {
            compositeDisposable.add(currencyRepository.getCurrencyConversions(value.toBigDecimal(), DEFAULT_CURRENCY, CONVERT_CURRENCIES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.i(ConversionViewModel::class.java.simpleName, "Conversions received!")
                        conversionStateData.value = it
                    }, {
                        Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
                        Log.e(ConversionViewModel::class.java.simpleName, "Error on getCurrencyConversions : ${it.message}", it)
                    }))
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
