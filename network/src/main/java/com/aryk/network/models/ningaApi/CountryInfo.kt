package com.aryk.network.models.ningaApi

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "countryInfo")
@Parcelize
data class CountryInfo(
    @PrimaryKey @SerializedName("_id") val id: Int,
    val lat: Double? = null,
    val long: Double? = null,
    val flag: String? = null,
    val iso3: String? = null,
    val iso2: String? = null
) : Parcelable
