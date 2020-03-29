package com.aryk.network.models.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryData(
    val country: String = "",
    val countryInfo: CountryInfo,
    val cases: Int = 0,
    val todayCases: Int = 0,
    val deaths: Int = 0,
    val todayDeaths: Int = 0,
    val recovered: Int = 0,
    val active: Int = 0,
    val critical: Int = 0,
    val casesPerOneMillion: Double = 0.0,
    val deathsPerOneMillion: Double = 0.0
) : Parcelable
