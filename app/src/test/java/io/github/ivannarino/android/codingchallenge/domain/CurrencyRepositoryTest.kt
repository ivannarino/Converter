package io.github.ivannarino.android.codingchallenge.domain

import android.accounts.NetworkErrorException
import io.github.ivannarino.android.codingchallenge.data.CurrencyDb
import io.github.ivannarino.android.codingchallenge.data.CurrencyWs
import io.github.ivannarino.android.codingchallenge.domain.model.Amount
import io.github.ivannarino.android.codingchallenge.domain.model.Conversion
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.reactivex.Flowable
import org.junit.*
import org.koin.test.AutoCloseKoinTest

class CurrencyRepositoryTest : AutoCloseKoinTest() {

    private lateinit var currencyRepository: CurrencyRepository

    private val currencyDb = mockk<CurrencyDb>()
    private val currencyWs = mockk<CurrencyWs>()

    @Before
    fun setUp() {
        currencyRepository = CurrencyRepository(currencyDb, currencyWs)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should not return values nor throw an error when db and ws return errors`() {
        // given
        val baseAmount = 1.toBigDecimal()
        val baseCurency = "USD"
        val toCurrencies = listOf("GBP", "EUR", "JPY", "BRL")
        every { currencyDb.getCurrencyConversions(baseCurency, any()) } returns Flowable.error(IllegalStateException())
        every { currencyWs.getCurrencyConversions(baseCurency, any()) } returns Flowable.error(NetworkErrorException())

        // when
        val testCurrencySubscriber = currencyRepository.getCurrencyConversions(baseAmount, baseCurency, toCurrencies).test()

        // then
        testCurrencySubscriber.awaitTerminalEvent()
        testCurrencySubscriber.assertTerminated()
        testCurrencySubscriber.assertNoValues()
        testCurrencySubscriber.assertNoErrors()
    }

    @Test
    fun `should return cache when ws returns errors`() {
        // given
        val baseAmount = 1.toBigDecimal()
        val baseCurency = "USD"
        val toCurrencies = listOf("GBP", "EUR", "JPY", "BRL")
        val baseCurrency = "USD"
        val amounts = listOf(Amount("BRL", 96.21.toBigDecimal()),
                Amount("JPY", 2704.40.toBigDecimal()),
                Amount("EUR", 22.50.toBigDecimal()),
                Amount("GBP", 19.63.toBigDecimal()))
        val returnedConversion = Conversion(Amount(baseCurrency, baseAmount), amounts)

        every { currencyDb.getCurrencyConversions(baseCurency, any()) } returns Flowable.just(returnedConversion)
        every { currencyWs.getCurrencyConversions(baseCurency, any()) } returns Flowable.error(NetworkErrorException())

        // when
        val testCurrencySubscriber = currencyRepository.getCurrencyConversions(baseAmount, baseCurency, toCurrencies).test()

        // then
        testCurrencySubscriber.awaitTerminalEvent()
        testCurrencySubscriber.assertTerminated()
        testCurrencySubscriber.assertNoErrors()
        testCurrencySubscriber.assertValueCount(1)
        testCurrencySubscriber.assertValue(returnedConversion)
    }

    @Test
    fun `should return just one value when both ws and db return`() {
        // given
        val baseAmount = 1.toBigDecimal()
        val baseCurency = "USD"
        val toCurrencies = listOf("GBP", "EUR", "JPY", "BRL")
        val baseCurrency = "USD"
        val amounts = listOf(Amount("BRL", 96.21.toBigDecimal()),
                Amount("JPY", 2704.40.toBigDecimal()),
                Amount("EUR", 22.50.toBigDecimal()),
                Amount("GBP", 19.63.toBigDecimal()))
        val returnedConversion = Conversion(Amount(baseCurrency, baseAmount), amounts)

        every { currencyDb.getCurrencyConversions(baseCurency, any()) } returns Flowable.just(returnedConversion)
        every { currencyWs.getCurrencyConversions(baseCurency, any()) } returns Flowable.just(returnedConversion)

        // when
        val testCurrencySubscriber = currencyRepository.getCurrencyConversions(baseAmount, baseCurency, toCurrencies).test()

        // then
        testCurrencySubscriber.awaitTerminalEvent()
        testCurrencySubscriber.assertTerminated()
        testCurrencySubscriber.assertNoErrors()
        testCurrencySubscriber.assertValueCount(1)
        testCurrencySubscriber.assertValue(returnedConversion)
    }
}