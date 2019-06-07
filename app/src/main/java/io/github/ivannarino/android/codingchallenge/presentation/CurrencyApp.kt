package io.github.ivannarino.android.codingchallenge.presentation

import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.ivannarino.android.codingchallenge.data.BigDecimalAdapter
import io.github.ivannarino.android.codingchallenge.data.CurrencyDatabase
import io.github.ivannarino.android.codingchallenge.data.CurrencyDb
import io.github.ivannarino.android.codingchallenge.data.CurrencyWs
import io.github.ivannarino.android.codingchallenge.domain.CurrencyRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class CurrencyApp : MultiDexApplication() {

    companion object {
        const val FIXER_IO_ACCESS_KEY = "bef61bd2358b869ded2e35e2ba3f82f8"
        const val FIXER_IO_BASE_URL = "http://data.fixer.io/api/"
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

        // Scheduling
        const val IO_SCHEDULER = "IO_SCHEDULER"
        const val MAIN_THREAD_SCHEDULER = "MAIN_THREAD_SCHEDULER"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@CurrencyApp)

            // module list
            modules(appMainModule)
        }
    }

    private val appMainModule = module {
        viewModel { ConversionViewModel(get(), androidApplication()) }
        single { CurrencyRepository(get(), get()) }
        single { CurrencyDb(get(), get()) }
        single { Moshi.Builder().add(BigDecimalAdapter()).add(KotlinJsonAdapterFactory()).build() }
        single { Room.databaseBuilder(androidContext(), CurrencyDatabase::class.java, DB_NAME).build() }
        single { CurrencyWs(get()) }
        single {
            Retrofit.Builder()
                .baseUrl(FIXER_IO_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        single(named(IO_SCHEDULER)) { Schedulers.io() }
        single(named(MAIN_THREAD_SCHEDULER)) { AndroidSchedulers.mainThread() }
    }
}