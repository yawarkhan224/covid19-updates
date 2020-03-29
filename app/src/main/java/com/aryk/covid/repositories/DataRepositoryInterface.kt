package com.aryk.covid.repositories

import com.aryk.network.models.data.CountryData

interface DataRepositoryInterface {
    suspend fun getAllCountriesData(sort: String?): List<CountryData>
    suspend fun getCountryData(country: String): CountryData
}
