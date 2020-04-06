package com.aryk.network

import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData
import com.aryk.network.models.ningaApi.OverAllData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VirusTrackerApiService {
    @GET("countries")
    suspend fun getAllCountriesData(
        @Query("sort") sort: String?
    ): List<CountryData>

    @GET("countries/{country}")
    suspend fun getCountryData(
        @Path("country") country: String
    ): CountryData

    @GET("v2/historical")
    suspend fun getHistoricalData(): List<CountryHistoricalData>

    @GET("all")
    suspend fun getOverAllData(): OverAllData
}
