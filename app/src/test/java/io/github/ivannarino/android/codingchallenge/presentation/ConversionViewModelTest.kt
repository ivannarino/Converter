package io.github.ivannarino.android.codingchallenge.presentation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.github.ivannarino.android.codingchallenge.domain.CurrencyRepository
import io.github.ivannarino.android.codingchallenge.domain.model.Amount
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.github.ivannarino.android.codingchallenge.presentation.util.hideSoftKeyboard
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.*
import org.junit.runner.*
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConversionViewModelTest : AutoCloseKoinTest() {

    private lateinit var currencyViewModel: ConversionViewModel

    private val currencyRepository = mockk<CurrencyRepository>()

    @Before
    fun setUp() {
        loadKoinModules(listOf(module {
            single(named(CurrencyApp.IO_SCHEDULER), override = true) { Schedulers.trampoline() }
            single(named(CurrencyApp.MAIN_THREAD_SCHEDULER), override = true) { Schedulers.trampoline() }
        }))
        currencyViewModel = ConversionViewModel(currencyRepository, ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should update conversion data when clicking on convert and amount is correct`() {
        // given
        val input = "25"
        val baseAmount = 25.toBigDecimal()
        val baseCurrency = "USD"
        val toCurrencies = listOf("GBP", "EUR", "JPY", "BRL")
        val amounts = listOf(Amount("BRL", 96.21.toBigDecimal()),
                Amount("JPY", 2704.40.toBigDecimal()),
                Amount("EUR", 22.50.toBigDecimal()),
                Amount("GBP", 19.63.toBigDecimal()))
        val returnedConversion = Conversion(Amount(baseCurrency, baseAmount), amounts)

        every { currencyRepository.getCurrencyConversions(baseAmount, baseCurrency, toCurrencies) } returns
                Flowable.just(returnedConversion)

        // when
        currencyViewModel.convert(ApplicationProvider.getApplicationContext(), input)

        // then
        val data = currencyViewModel.getConversionStateData().value!!
        assert(data == returnedConversion)
    }

    @Test
    fun `should not update data when clicking on convert and amount is incorrect`() {
        // given
        val input = "INCORRECT INPUT"
        val baseAmount = "25".toBigDecimal()
        val baseCurrency = "USD"
        val toCurrencies = listOf("GBP", "EUR", "JPY", "BRL")
        val amounts = listOf(Amount("BRL", 96.21.toBigDecimal()),
                Amount("JPY", 2704.40.toBigDecimal()),
                Amount("EUR", 22.50.toBigDecimal()),
                Amount("GBP", 19.63.toBigDecimal()))
        val returnedConversion = Conversion(Amount(baseCurrency, baseAmount), amounts)

        every { currencyRepository.getCurrencyConversions(baseAmount, baseCurrency, toCurrencies) } returns
                Flowable.just(returnedConversion)

        // when
        currencyViewModel.convert(ApplicationProvider.getApplicationContext(), input)

        // then
        val data = currencyViewModel.getConversionStateData().value
        assert(data == null)
    }

    @Test
    fun `should hide keyboard when clicking on convert`() {
        // given
        mockkStatic("io.github.ivannarino.android.codingchallenge.presentation.util.AndroidExtKt")
        val context = mockk<Context>(relaxed = true)
        val baseAmount = 25.toBigDecimal()
        val baseCurrency = "USD"
        val amounts = listOf(Amount("BRL", 96.21.toBigDecimal()),
                Amount("JPY", 2704.40.toBigDecimal()),
                Amount("EUR", 22.50.toBigDecimal()),
                Amount("GBP", 19.63.toBigDecimal()))
        val returnedConversion = Conversion(Amount(baseCurrency, baseAmount), amounts)

        every { currencyRepository.getCurrencyConversions(any(), any(), any()) } returns
                Flowable.just(returnedConversion)

        // when
        currencyViewModel.convert(context, "25")

        // then
        verify {
            context.hideSoftKeyboard(any())
        }
    }
}