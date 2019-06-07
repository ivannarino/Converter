package io.github.ivannarino.android.codingchallenge.presentation.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideSoftKeyboard(view: View? = null) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken ?: (this as Activity).findViewById<View>(android.R.id.content).windowToken, 0)
}