package io.github.ivannarino.android.codingchallenge.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Keep
@Parcelize
data class Amount(val currency: String, val value: BigDecimal) : Parcelable