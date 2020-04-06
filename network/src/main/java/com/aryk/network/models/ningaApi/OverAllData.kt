package com.aryk.network.models.ningaApi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OverAllData(
    val cases: Int? = null,
    val deaths: Int? = null,
    val recovered: Int? = null,
    val updated: Long? = null,
    val active: Int? = null
) : Parcelable
