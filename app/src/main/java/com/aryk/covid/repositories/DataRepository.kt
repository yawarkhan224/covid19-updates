package com.aryk.covid.repositories

import com.aryk.network.Covid19Service
import com.aryk.network.models.data.CountryData

class DataRepository(
    val covid19Service: Covid19Service
) : DataRepositoryInterface {
    override suspend fun getAllCountriesData(sort: String?): List<CountryData> {
        return covid19Service.getAllCountriesData(sort)
    }

    override suspend fun getCountryData(country: String): CountryData {
        TODO("Not yet implemented")
    }
}
