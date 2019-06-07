package io.github.ivannarino.android.codingchallenge.presentation

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import io.github.ivannarino.android.codingchallenge.R
import io.github.ivannarino.android.codingchallenge.domain.CurrencyRepository
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
@MediumTest
class ConversionFragmentTest : KoinTest {

    @get:Rule
    val mainActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    private val currencyRepository = mockk<CurrencyRepository>(relaxed = true)

    @Before
    fun setup() {
        loadKoinModules(listOf(module {
            viewModel(override = true) { ConversionViewModel(currencyRepository, getApplicationContext()) }
        }))
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun should_display_convert_button_and_hint_when_opening_the_conversion_page() {
        // given

        // when
        mainActivityRule.launchActivity(null)

        // then
        onView(withText(R.string.convert)).check(matches(allOf(isClickable(), isDisplayed())))
        onView(
            withHint(
                getApplicationContext<CurrencyApp>().getString(
                    R.string.amount_in_currency,
                    CurrencyApp.DEFAULT_CURRENCY
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )


    }

    @Test
    fun should_display_title_when_opening_the_conversion_page() {
        // given

        // when
        mainActivityRule.launchActivity(null)

        // then
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))
    }

    @Test
    fun should_call_repository_when_clicking_on_the_convert_button() {
        // given
        mainActivityRule.launchActivity(null)
        val typedAmountToConvert = "5"

        // when
        onView(withId(R.id.amountEditText)).perform(typeText(typedAmountToConvert))
        onView(withText(R.string.convert)).perform(click())

        // then
        verify(exactly = 1) {
            currencyRepository.getCurrencyConversions(
                typedAmountToConvert.toBigDecimal(),
                CurrencyApp.DEFAULT_CURRENCY,
                CurrencyApp.CONVERT_CURRENCIES
            )
        }
    }

    @Test
    fun should_not_crash_when_typing_non_numeric_input() {
        // given
        mainActivityRule.launchActivity(null)
        val typedAmountToConvert = "INVALID INPUT"

        // when
        onView(withId(R.id.amountEditText)).perform(typeText(typedAmountToConvert))
        onView(withText(R.string.convert)).perform(click())

        // then
    }

}