package com.aryk.covid.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FormattedHistoricalData(
    val cases: Map<Int, Int> = mutableMapOf(),
    val deaths: Map<Int, Int> = mutableMapOf(),
    val recovered: Map<Int, Int> = mutableMapOf()
) : Parcelable