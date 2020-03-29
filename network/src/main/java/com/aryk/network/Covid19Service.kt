package com.aryk.network

import com.aryk.network.models.data.CountryData
import com.aryk.network.models.data.CountryHistoricalData
import com.aryk.network.models.data.OverAllData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Covid19Service {
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
