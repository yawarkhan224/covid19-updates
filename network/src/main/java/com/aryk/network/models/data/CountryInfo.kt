package com.aryk.network.models.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryInfo(
    @SerializedName("_id") val id: Int = 0,
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val flag: String = "",
    val iso3: String = "",
    val iso2: String = ""
) : Parcelable
