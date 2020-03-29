package com.aryk.network.models.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimelineData(
    val cases: Map<String, Int> = mutableMapOf(),
    val deaths: Map<String, Int> = mutableMapOf()
) : Parcelable
