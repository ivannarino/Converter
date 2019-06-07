package io.github.ivannarino.android.codingchallenge.data

import androidx.room.*
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.reactivex.Flowable
import io.reactivex.Maybe
import java.math.BigDecimal

class CurrencyDb(private val moshi: Moshi, private val currencyDatabase: CurrencyDatabase) {

    fun getCurrencyConversions(baseCurrency: String, currencies: List<String>): Flowable<Conversion> {
        return currencyDatabase.currencyConversionDao().getCurrencyConversion(baseCurrency,
                currencies.joinToString(",")).flatMapPublisher {
            Flowable.just(deserializeConversion(it.value))
        }
    }

    fun saveCurrencyConversions(baseCurrency: String, currencies: List<String>, conversion: Conversion) {
        return currencyDatabase.currencyConversionDao().save(CurrencyConversion(baseCurrency = baseCurrency,
                currencies = currencies.joinToString(","), value = serializeConversion(conversion)))
    }

    private fun serializeConversion(conversion: Conversion): String {
        val jsonAdapter = moshi.adapter<Conversion>(Conversion::class.java)
        return jsonAdapter.toJson(conversion)
    }

    private fun deserializeConversion(conversion: String): Conversion? {
        val jsonAdapter = moshi.adapter(Conversion::class.java)
        return jsonAdapter.fromJson(conversion)
    }
}

@Entity(tableName = "currency_conversion")
data class CurrencyConversion(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val baseCurrency: String,
        val currencies: String,
        val value: String
)

@Database(entities = [CurrencyConversion::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyConversionDao(): CurrencyConversionDao
}

@Dao
interface CurrencyConversionDao {
    @Query("SELECT * FROM currency_conversion WHERE baseCurrency = :baseCurrency AND currencies = :currencies ORDER BY ROWID ASC LIMIT 1")
    fun getCurrencyConversion(baseCurrency: String, currencies: String): Maybe<CurrencyConversion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(data: CurrencyConversion)
}

class BigDecimalAdapter {
    @FromJson
    fun fromJson(string: String) = BigDecimal(string)

    @ToJson
    fun toJson(value: BigDecimal) = value.toString()
}