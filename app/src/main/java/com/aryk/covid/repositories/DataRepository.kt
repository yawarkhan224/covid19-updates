package com.aryk.covid.repositories

import com.aryk.covid.enums.CountriesSortTypeEnum
import com.aryk.network.Covid19Service
import com.aryk.network.models.data.CountryData
import com.aryk.network.models.data.CountryHistoricalData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(
    val covid19Service: Covid19Service
) : DataRepositoryInterface {
    override suspend fun getAllCountriesData(sort: String?): List<CountryData> {
        return withContext(Dispatchers.IO) {
            covid19Service.getAllCountriesData(sort ?: CountriesSortTypeEnum.Cases.name)
        }
    }

    override suspend fun getCountryData(country: String): CountryData {
        return withContext(Dispatchers.IO) {
            covid19Service.getCountryData(country)
        }
    }

    override suspend fun getHistoricalData(): List<CountryHistoricalData> {
        return withContext(Dispatchers.IO) {
            covid19Service.getHistoricalData()
        }
    }
}
