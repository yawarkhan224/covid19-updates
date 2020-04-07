package com.aryk.covid.repositories

import com.aryk.covid.enums.CountriesSortType
import com.aryk.network.NingaApiService
import com.aryk.network.VirusTrackerApiService
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class DataRepository(
    private val ningaService: NingaApiService,
    private val virusTrackerService: VirusTrackerApiService
) : DataRepositoryInterface {
    override suspend fun getAllCountriesData(sort: String?): Flow<List<CountryData>> {
        return flow {
            // execute API call
            val data = ningaService.getAllCountriesData(sort ?: CountriesSortType.Cases.name)

            // Emit the list to the stream
            emit(data)
        }.flowOn(Dispatchers.IO) // Use the IO thread for this Flow
    }

    override suspend fun getCountryData(country: String): Flow<CountryData> {
        return flow {
            // execute API call
            val data = ningaService.getCountryData(country)

            // Emit the list to the stream
            emit(data)
        }.flowOn(Dispatchers.IO) // Use the IO thread for this Flow
    }

    override suspend fun getHistoricalData(): List<CountryHistoricalData> {
        return withContext(Dispatchers.IO) {
            ningaService.getHistoricalData()
        }
    }

    override suspend fun getHistoricalData2(countryISO2: String): Flow<CountryTimelineResponse> {
        return flow {
            // execute API call
            val data = virusTrackerService.getCountryTimeline(countryISO2)

            // Emit the list to the stream
            emit(data)
        }.flowOn(Dispatchers.IO) // Use the IO thread for this Flow
    }
}
