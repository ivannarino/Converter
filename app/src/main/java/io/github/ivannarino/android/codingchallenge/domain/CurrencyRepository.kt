package io.github.ivannarino.android.codingchallenge.domain

import io.github.ivannarino.android.codingchallenge.data.CurrencyDb
import io.github.ivannarino.android.codingchallenge.data.CurrencyWs
import io.github.ivannarino.android.codingchallenge.domain.model.Amount
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import java.math.BigDecimal

class CurrencyRepository(currencyDb: CurrencyDb, currencyWs: CurrencyWs) {

    fun getCurrencyConversions(baseValue: BigDecimal, baseCurrency: String, currencies: List<String>): Conversion {
        return Conversion(
                Amount(baseCurrency, baseValue),
                listOf(
                        Amount("GBP", 19.71.toBigDecimal()),
                        Amount("EUR", 22.27.toBigDecimal()),
                        Amount("JPY", 2709.33.toBigDecimal()),
                        Amount("BRL", 97.04.toBigDecimal())
                )
        )
    }
}