package com.aryk.network.models.virusTrackerApi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryTimelineItem(
    @SerializedName("new_daily_cases") val newDailyCases: Int? = null,
    @SerializedName("new_daily_deaths") val newDailyDeaths: Int? = null,
    @SerializedName("total_cases") val totalCases: Int? = null,
    @SerializedName("total_recoveries") val totalRecoveries: Int? = null,
    @SerializedName("total_deaths") val totalDeaths: Int? = null
) : Parcelable
