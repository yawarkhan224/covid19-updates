package com.aryk.covid.extensions

import com.aryk.covid.models.FormattedTimelineData
import com.aryk.network.models.virusTrackerApi.CountryTimelineItem
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import com.google.gson.Gson

@SuppressWarnings("MagicNumber", "LongMethod")
fun CountryTimelineResponse.toFormattedTimelineData(): FormattedTimelineData {
    val casesMap: MutableMap<Float, Float> = mutableMapOf()
    val deathsMap: MutableMap<Float, Float> = mutableMapOf()
    val recoveredMap: MutableMap<Float, Float> = mutableMapOf()
    val gson: Gson = Gson()
    this.timelineItems?.forEach { rootMap ->
        val dateList: MutableList<String> = rootMap.keys.toMutableList()

        dateList.forEach { dateValue ->
            // Skip any key which is not of date size
            if (dateValue.length > 6) {
                val (m, d, _) = dateValue.split("/")
                val item: CountryTimelineItem =
                    gson.fromJson(gson.toJson(rootMap[dateValue]), CountryTimelineItem::class.java)
                casesMap[("$m.$d").toFloat()] = item.totalCases?.toFloat() ?: 0f
                deathsMap[("$m.$d").toFloat()] = item.totalDeaths?.toFloat() ?: 0f
                recoveredMap[("$m.$d").toFloat()] = item.totalRecoveries?.toFloat() ?: 0f
            }
        }
    }

    return FormattedTimelineData(
        casesMap,
        deathsMap,
        recoveredMap
    )
}
