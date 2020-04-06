package com.aryk.covid.extensions

// import com.aryk.covid.models.FormattedHistoricalData
// import com.aryk.network.models.ningaApi.TimelineData
// import java.util.Calendar

// @SuppressWarnings("MagicNumber", "LongMethod")
// fun TimelineData.toFormattedData(): FormattedHistoricalData {
//    val casesKeys = this.cases?.keys
//    val deathsKeys = this.deaths?.keys
//    val recoveredKeys = this.recovered?.keys
//
//    var toReturn: FormattedHistoricalData?
//    val casesMap: MutableMap<Int, Int> = mutableMapOf()
//    val deathsMap: MutableMap<Int, Int> = mutableMapOf()
//    val recoveredMap: MutableMap<Int, Int> = mutableMapOf()
//
//    if (casesKeys.isNotEmpty()) {
//        val (m, d, y) = casesKeys.first().split("/")
//        val calendar = Calendar.getInstance()
//        calendar.set((y + y).toInt(), m.toInt() - 1, d.toInt(), 0, 0)
//        val week1 = calendar.get(Calendar.WEEK_OF_YEAR)
//        val dow = calendar.get(Calendar.DAY_OF_WEEK)
//
//        var weekCount = week1
//
//        val casesMapToList = this.cases.toList()
//
//        var x = dow
//        while (x <= recoveredKeys.size) {
//            println(x)
//            if (x == dow) {
//                casesMap[week1] = casesMapToList[x].second
//                x += 7 - dow
//            } else {
//                casesMap[weekCount] = casesMapToList[x].second
//                x += 7
//            }
//            weekCount += 1
//        }
//    }
//
//    if (deathsKeys.isNotEmpty()) {
//        val (m, d, y) = casesKeys.first().split("/")
//        val calendar = Calendar.getInstance()
//        calendar.set((y + y).toInt(), m.toInt() - 1, d.toInt(), 0, 0)
//        val week1 = calendar.get(Calendar.WEEK_OF_YEAR)
//        val dow = calendar.get(Calendar.DAY_OF_WEEK)
//
//        var weekCount = week1
//
//        val deathsMapToList = this.deaths.toList()
//
//        var x = dow
//        while (x <= recoveredKeys.size) {
//            println(x)
//            if (x == dow) {
//                deathsMap[week1] = deathsMapToList[x].second
//                x += 7 - dow
//            } else {
//                deathsMap[weekCount] = deathsMapToList[x].second
//                x += 7
//            }
//            weekCount += 1
//        }
//    }
//
//    if (recoveredKeys.isNotEmpty()) {
//        val (m, d, y) = casesKeys.first().split("/")
//        val calendar = Calendar.getInstance()
//        calendar.set((y + y).toInt(), m.toInt() - 1, d.toInt(), 0, 0)
//        val week1 = calendar.get(Calendar.WEEK_OF_YEAR)
//        val dow = calendar.get(Calendar.DAY_OF_WEEK)
//
//        var weekCount = week1
//
//        val recoveredMapToList = this.recovered.toList()
//
//        var x = dow
//        while (x <= recoveredKeys.size) {
//            println(x)
//            if (x == dow) {
//                recoveredMap[week1] = recoveredMapToList[x].second
//                x += 7 - dow
//            } else {
//                recoveredMap[weekCount] = recoveredMapToList[x].second
//                x += 7
//            }
//            weekCount += 1
//        }
//    }
//
//    toReturn = FormattedHistoricalData(
//        casesMap,
//        deathsMap,
//        recoveredMap
//    )
//
//    return toReturn
// }
