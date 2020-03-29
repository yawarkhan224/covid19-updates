package com.aryk.network.models.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryHistoricalData(
    val country: String = "",
    val province: String? = null,
    val timeline: TimelineData
) : Parcelable
