package io.github.ivannarino.android.codingchallenge.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Conversion(val fromAmount: Amount, val conversions: List<Amount>) : Parcelable