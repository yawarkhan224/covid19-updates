package com.aryk.network

import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface VirusTrackerApiService {
    @GET("free-api")
    suspend fun getCountryTimeline(
        @Query("countryTimeline") countryTimeline: String
    ): CountryTimelineResponse
}
