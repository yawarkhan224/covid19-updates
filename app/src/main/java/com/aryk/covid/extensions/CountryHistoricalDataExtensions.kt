package com.aryk.covid.extensions

import com.aryk.covid.models.FormattedTimelineData
import com.aryk.network.models.ningaApi.CountryHistoricalData

@SuppressWarnings("MagicNumber", "LongMethod")
fun CountryHistoricalData.toFormattedTimelineData(): FormattedTimelineData {
    var casesMap: MutableMap<Float, Float> = mutableMapOf()
    var deathsMap: MutableMap<Float, Float> = mutableMapOf()
    var recoveredMap: MutableMap<Float, Float> = mutableMapOf()
    this.timeline?.let { rootMap ->
        // Cases Dates List
        val cdl: MutableList<String> = rootMap.cases?.keys?.toMutableList() ?: mutableListOf()

        // Deaths Dates List
        val ddl: MutableList<String> = rootMap.cases?.keys?.toMutableList() ?: mutableListOf()

        // Recovered Dates List
        val rdl: MutableList<String> = rootMap.cases?.keys?.toMutableList() ?: mutableListOf()

        casesMap = formatEntity(cdl, rootMap.cases!!)
        deathsMap = formatEntity(ddl, rootMap.deaths!!)
        recoveredMap = formatEntity(rdl, rootMap.recovered!!)
    }

    return FormattedTimelineData(
        casesMap,
        deathsMap,
        recoveredMap
    )
}

private fun formatEntity(
    keyList: MutableList<String>,
    rootMap: Map<String, Int>
): MutableMap<Float, Float> {
    var lastDay = 0
    var lastMonth = 1
    val dataMap: MutableMap<Float, Float> = mutableMapOf()
    keyList.forEach {
        val (month, day, _) = it.split("/")
        val key: Float
        when {
            lastDay == 0 -> {
                lastDay = day.toInt()
                key = day.toFloat()
            }
            month.toInt() > lastMonth -> {
                lastDay += day.toInt()
                key = lastDay.toFloat()
            }
            else -> {
                lastDay += 1
                key = lastDay.toFloat()
            }
        }

        lastMonth = month.toInt()
        dataMap[key] = rootMap[it]?.toFloat() ?: 0f
    }

    return dataMap
}
