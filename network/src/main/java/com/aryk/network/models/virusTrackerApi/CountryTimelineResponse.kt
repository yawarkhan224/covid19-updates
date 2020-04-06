package com.aryk.network.models.virusTrackerApi

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler

@Parcelize
@TypeParceler<Map<String, Any>, MapStringAnyParceler>
data class CountryTimelineResponse(
    @SerializedName("countrytimelinedata") val countryTimelineData: List<CountryTimelineInfo>? = null,
    @SerializedName("timelineitems") val timelineItems: List<Map<String, Any>>? = null
) : Parcelable

object MapStringAnyParceler : Parceler<Map<String, Any>> {
    override fun create(parcel: Parcel): Map<String, Any> {
        val attributes: Map<String, Any> = mutableMapOf()
        parcel.readMap(attributes, Any::class.java.classLoader)

        return attributes
    }

    override fun Map<String, Any>.write(parcel: Parcel, flags: Int) {
        parcel.writeMap(this)
    }
}
