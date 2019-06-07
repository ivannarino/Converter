package io.github.ivannarino.android.codingchallenge.domain

import android.util.Log
import io.github.ivannarino.android.codingchallenge.data.CurrencyDb
import io.github.ivannarino.android.codingchallenge.data.CurrencyWs
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.DEBOUNCE_TIMEOUT
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class CurrencyRepository(private val currencyDb: CurrencyDb, private val currencyWs: CurrencyWs) {

    fun getCurrencyConversions(baseValue: BigDecimal, baseCurrency: String, currencies: List<String>): Flowable<Conversion> {
        return Flowable.concatArray(
                getCurrencyForCurrencyUnitFromDb(baseCurrency, currencies).onErrorResumeNext { _: Throwable ->
                    Flowable.empty<Conversion>()
                },
                getCurrencyForCurrencyUnitFromWs(baseCurrency, currencies).onErrorResumeNext { _: Throwable ->
                    Flowable.empty<Conversion>()
                })
                .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .map {
                    Conversion(
                            fromAmount = it.fromAmount.copy(value = baseValue),
                            conversions = it.conversions.map { it.copy(value = it.value * baseValue) })
                }
    }

    private fun getCurrencyForCurrencyUnitFromWs(baseCurrency: String, currencies: List<String>): Flowable<Conversion> {
        return currencyWs.getCurrencyConversions(baseCurrency, currencies).doOnNext {
            Log.i(CurrencyRepository::class.java.simpleName, "Dispatching from WS...")
            currencyDb.saveCurrencyConversions(baseCurrency, currencies, it)
        }
    }

    private fun getCurrencyForCurrencyUnitFromDb(baseCurrency: String, currencies: List<String>): Flowable<Conversion> {
        return currencyDb.getCurrencyConversions(baseCurrency, currencies).doOnNext {
            Log.i(CurrencyRepository::class.java.simpleName, "Dispatching from DB...")
        }
    }
}
