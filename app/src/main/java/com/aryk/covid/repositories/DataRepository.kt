package com.aryk.covid.repositories

import com.aryk.covid.enums.CountriesSortType
import com.aryk.network.NingaApiService
import com.aryk.network.VirusTrackerApiService
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DataRepository(
    val ningaService: NingaApiService,
    val virusTrackerService: VirusTrackerApiService
) : DataRepositoryInterface {
    override suspend fun getAllCountriesData(sort: String?): List<CountryData> {
        return withContext(Dispatchers.IO) {
            ningaService.getAllCountriesData(sort ?: CountriesSortType.Cases.name)
        }
    }

    override suspend fun getCountryData(country: String): CountryData {
        return withContext(Dispatchers.IO) {
            ningaService.getCountryData(country)
        }
    }

    override suspend fun getHistoricalData(): List<CountryHistoricalData> {
        return withContext(Dispatchers.IO) {
            ningaService.getHistoricalData()
        }
    }

    override suspend fun getHistoricalData2(countryISO2: String): CountryTimelineResponse {

        // exectute API call and map to UI object
        return virusTrackerService.getCountryTimeline(countryISO2)
    }
}
