package com.aryk.covid.enums

enum class CountriesSortType(val key: String) {
    Country("country"),
    Cases("cases"),
    Deaths("deaths"),
    Recovered("recovered");

    companion object {
        fun fromSorterName(name: String?): CountriesSortType? {
            return values().find { sorterType ->
                sorterType.name == name
            }
        }

        fun fromSorterKey(key: String?): CountriesSortType? {
            return values().find { sorterType ->
                sorterType.key == key
            }
        }
    }
}
