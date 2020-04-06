package com.aryk.network.models.ningaApi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryData(
    val country: String? = null,
    val countryInfo: CountryInfo? = null,
    val cases: Int? = null,
    val todayCases: Int? = null,
    val deaths: Int? = null,
    val todayDeaths: Int? = null,
    val recovered: Int? = null,
    val active: Int? = null,
    val critical: Int? = null,
    val casesPerOneMillion: Double? = null,
    val deathsPerOneMillion: Double? = null,
    val updated: String? = null
) : Parcelable
