package com.aryk.network.models.ningaApi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimelineData(
    val cases: Map<String, Int>? = null,
    val deaths: Map<String, Int>? = null,
    val recovered: Map<String, Int>? = null
) : Parcelable
