package io.github.ivannarino.android.codingchallenge.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.github.ivannarino.android.codingchallenge.presentation.CurrencyApp.Companion.FIXER_IO_ACCESS_KEY
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class CurrencyWs(private val retrofitClient: Retrofit) {

    fun getCurrencyConversions(currencies: List<String>): Observable<CurrencyResult> {
        return retrofitClient.create(CurrencyApi::class.java).getCurrencyConversions(FIXER_IO_ACCESS_KEY, currencies.joinToString(separator = ","))
    }
}

interface CurrencyApi {
    @GET("latest")
    fun getCurrencyConversions(@Query("access_key") accessKey: String, @Query("symbols") id: String): Observable<CurrencyResult>
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