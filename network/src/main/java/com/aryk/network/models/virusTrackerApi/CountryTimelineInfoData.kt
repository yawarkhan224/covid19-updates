package com.aryk.network.models.virusTrackerApi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryTimelineInfoData(
    @SerializedName("ourid") val ourId: Int? = null,
    val title: String? = null,
    val code: String? = null,
    val source: String? = null
) : Parcelable
