package com.aryk.network.models.virusTrackerApi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryTimelineInfo(
    val info: CountryTimelineInfoData? = null
) : Parcelable
