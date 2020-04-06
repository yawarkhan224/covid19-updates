package com.aryk.network.models.ningaApi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryInfo(
    @SerializedName("_id") val id: Int? = null,
    val lat: Double? = null,
    val long: Double? = null,
    val flag: String? = null,
    val iso3: String? = null,
    val iso2: String? = null
) : Parcelable
