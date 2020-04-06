package com.aryk.covid.repositories

import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData

interface DataRepositoryInterface {
    suspend fun getAllCountriesData(sort: String?): List<CountryData>
    suspend fun getCountryData(country: String): CountryData
    suspend fun getHistoricalData(): List<CountryHistoricalData>
}
