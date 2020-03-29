package com.aryk.network.models.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OverAllData(
    val cases: Int = 0,
    val deaths: Int = 0,
    val recovered: Int = 0,
    val updated: Long = 0,
    val active: Int = 0
) : Parcelable
