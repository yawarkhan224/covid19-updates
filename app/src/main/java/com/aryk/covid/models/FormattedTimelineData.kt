package com.aryk.covid.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FormattedTimelineData(
    val cases: Map<Float, Float> = mutableMapOf(),
    val deaths: Map<Float, Float> = mutableMapOf(),
    val recovered: Map<Float, Float> = mutableMapOf()
) : Parcelable
