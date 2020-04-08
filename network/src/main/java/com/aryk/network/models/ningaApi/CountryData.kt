package com.aryk.network.models.ningaApi

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Countries")
data class CountryData(
    @PrimaryKey val country: String,
    @TypeConverters(CountryInfoConverter::class) val countryInfo: CountryInfo? = null,
    val cases: Int? = null,
    val todayCases: Int? = null,
    val deaths: Int? = null,
    val todayDeaths: Int? = null,
    val recovered: Int? = null,
    val active: Int? = null,
    val critical: Int? = null,
    val casesPerOneMillion: Double? = null,
    val deathsPerOneMillion: Double? = null,
    val updated: String? = null,
    val tests: Int? = null,
    val testsPerOneMillion: Double? = null
) : Parcelable

class CountryInfoConverter {
    @TypeConverter
    fun toString(data: CountryInfo?): String? {
        return data?.let {
            val gson = Gson()
            gson.toJson(data)
        } ?: run {
            null
        }
    }

    @TypeConverter
    fun fromString(data: String?): CountryInfo? {
        return data?.let {
            val gson = Gson()
            gson.fromJson(data, CountryInfo::class.java)
        } ?: run {
            null
        }
    }
}
