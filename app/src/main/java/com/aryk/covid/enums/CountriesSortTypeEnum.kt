package com.aryk.covid.enums

enum class CountriesSortTypeEnum(private val sortBy: String) {
    Cases("cases"),
    TodayCases("todayCases"),
    Deaths("deaths"),
    TodayDeaths("todayDeaths"),
    Recovered("recovered"),
    Active("active"),
    Critical("critical"),
    CasesPerOneMillion("casesPerOneMillion"),
    DeathsPerOneMillion("deathsPerOneMillion")
}
