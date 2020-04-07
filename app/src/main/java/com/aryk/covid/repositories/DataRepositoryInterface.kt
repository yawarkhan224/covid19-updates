package com.aryk.covid.repositories

import com.aryk.network.Result
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import kotlinx.coroutines.flow.Flow

interface DataRepositoryInterface {
    suspend fun getAllCountriesData(sort: String?): List<CountryData>
    suspend fun getCountryData(country: String): CountryData
    suspend fun getHistoricalData(): List<CountryHistoricalData>
    suspend fun getHistoricalData2(countryISO2: String): CountryTimelineResponse
}
