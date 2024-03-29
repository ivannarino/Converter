package io.github.ivannarino.android.codingchallenge.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.ivannarino.android.codingchallenge.domain.model.Amount
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.DEFAULT_CURRENCY
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.FIXER_IO_ACCESS_KEY
import io.reactivex.Flowable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class CurrencyWs(private val retrofitClient: Retrofit) {

    fun getCurrencyConversions(baseCurrency: String, currencies: List<String>): Flowable<Conversion> {
        // trick to change the baseCurrency to USD because Fixer.io cannot let you change the baseCurrency in the free plan
        return retrofitClient.create(CurrencyApi::class.java).getCurrencyConversions(FIXER_IO_ACCESS_KEY,
                (currencies + DEFAULT_CURRENCY).joinToString(separator = ",")).map { currencyResult ->
            val euroToDefaultCurrency = currencyResult.rates.getValue(DEFAULT_CURRENCY)
            val conversionsWithoutDefaultCurrency =
                    currencyResult.copy(rates = currencyResult.rates.filterNot { it.key == DEFAULT_CURRENCY })

            Conversion(Amount(baseCurrency, 1.toBigDecimal()),
                    conversionsWithoutDefaultCurrency.rates.map {
                        Amount(it.key, it.value.toBigDecimal() / euroToDefaultCurrency.toBigDecimal()
                        )
                    })
        }
    }
}

interface CurrencyApi {
    @GET("latest")
    fun getCurrencyConversions(@Query("access_key") accessKey: String, @Query("symbols") id: String): Flowable<CurrencyResult>
}

@JsonClass(generateAdapter = true)
data class CurrencyResult(
        @Json(name = "base")
        val base: String = "",
        @Json(name = "date")
        val date: String = "",
        @Json(name = "rates")
        val rates: Map<String, Double> = emptyMap(),
        @Json(name = "success")
        val success: Boolean = false,
        @Json(name = "timestamp")
        val timestamp: Int = 0
)