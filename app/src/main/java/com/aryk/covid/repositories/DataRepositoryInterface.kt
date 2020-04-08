package com.aryk.covid.repositories

import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import kotlinx.coroutines.flow.Flow

interface DataRepositoryInterface {
    suspend fun getAllCountriesData(sort: String?): Flow<List<CountryData>>
    suspend fun getCountryData(country: String): Flow<CountryData>
    suspend fun getHistoricalData(countryISO2: String): Flow<CountryTimelineResponse>
}
