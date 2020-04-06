package com.aryk.network.models.virusTrackerApi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryTimelineItem(
    val info: CountryTimelineInfoData? = null
) : Parcelable
