package io.github.ivannarino.android.codingchallenge.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.ivannarino.android.codingchallenge.domain.CurrencyRepository
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.CONVERT_CURRENCIES
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.DEFAULT_CURRENCY
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.IO_SCHEDULER
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.MAIN_THREAD_SCHEDULER
import io.github.ivannarino.android.codingchallenge.presentation.util.hideSoftKeyboard
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class ConversionViewModel(private val currencyRepository: CurrencyRepository, application: Application) : AndroidViewModel(application), KoinComponent {

    private val conversionStateData = MutableLiveData<Conversion>()

    private val compositeDisposable = CompositeDisposable()

    fun getConversionStateData(): LiveData<Conversion> = conversionStateData

    fun convert(context: Context, value: String) {
        context.hideSoftKeyboard()

        value.toBigIntegerOrNull()?.let {
            compositeDisposable.add(currencyRepository.getCurrencyConversions(value.toBigDecimal(), DEFAULT_CURRENCY, CONVERT_CURRENCIES)
                    .subscribeOn(get(named(IO_SCHEDULER)))
                    .observeOn(get(named(MAIN_THREAD_SCHEDULER)))
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
