package com.aryk.covid.enums

enum class CountriesSortType(val key: String) {
    Country("country"),
    Cases("cases"),
    Deaths("deaths"),
    Recovered("recovered");

    companion object {
        fun fromSorterType(key: String?): CountriesSortType? {
            return CountriesSortType.values().find { sorterType ->
                sorterType.key == key
            }
        }
    }
}
