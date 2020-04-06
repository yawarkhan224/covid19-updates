package com.aryk.network.models.ningaApi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryHistoricalData(
    val country: String? = null,
    val province: String? = null,
    val timeline: TimelineData? = null
) : Parcelable