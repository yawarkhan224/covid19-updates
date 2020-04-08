package com.aryk.covid.persistance.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aryk.network.models.ningaApi.CountryData

@Dao
interface CountryDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(country: List<CountryData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: CountryData)

    @Update
    suspend fun updateCountry(country: CountryData)

    @Delete
    suspend fun deleteCountry(country: CountryData)

    @Query("SELECT * FROM Countries WHERE country == :name")
    suspend fun getCountryByName(name: String): CountryData?

    @Query("SELECT * FROM Countries")
    suspend fun getCountries(): List<CountryData>
}
