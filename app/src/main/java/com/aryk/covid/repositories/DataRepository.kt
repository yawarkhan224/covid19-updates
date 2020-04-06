package com.aryk.covid.repositories

import com.aryk.covid.enums.CountriesSortType
import com.aryk.network.NingaApiService
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    val ningaApiService: NingaApiService
) : DataRepositoryInterface {
    override suspend fun getAllCountriesData(sort: String?): List<CountryData> {
        return withContext(Dispatchers.IO) {
            ningaApiService.getAllCountriesData(sort ?: CountriesSortType.Cases.name)
        }
    }

    override suspend fun getCountryData(country: String): CountryData {
        return withContext(Dispatchers.IO) {
            ningaApiService.getCountryData(country)
        }
    }

    override suspend fun getHistoricalData(): List<CountryHistoricalData> {
        return withContext(Dispatchers.IO) {
            ningaApiService.getHistoricalData()
        }
    }
}
