package com.aryk.covid.repositories

import com.aryk.covid.persistance.LocalDatabase
import com.aryk.network.NingaApiService
import com.aryk.network.VirusTrackerApiService
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class DataRepository(
    private val ningaService: NingaApiService,
    private val virusTrackerService: VirusTrackerApiService,
    private val localDatabase: LocalDatabase
) : DataRepositoryInterface {
    override suspend fun getAllCountriesData(sort: String?): Flow<List<CountryData>> {
        return flow {
            // execute API call
            val data = ningaService.getAllCountriesData(sort)

            // Emit the list to the stream
            localDatabase.countryDataDao().insertCountries(
                data.sortedWith(compareBy<CountryData> { -(it.cases ?: 0) })
            )
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

    override suspend fun getHistoricalData(countryISO2: String): Flow<CountryTimelineResponse> {
        return flow {
            // execute API call
            val data = virusTrackerService.getCountryTimeline(countryISO2)

            // Emit the list to the stream
            emit(data)
        }.flowOn(Dispatchers.IO) // Use the IO thread for this Flow
    }
}
