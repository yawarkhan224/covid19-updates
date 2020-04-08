package com.aryk.covid.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aryk.covid.persistance.daos.CountryDataDao
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryInfo
import com.aryk.network.models.ningaApi.CountryInfoConverter

@Database(entities = [CountryData::class, CountryInfo::class], version = 1)
@TypeConverters(CountryInfoConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun countryDataDao(): CountryDataDao

    companion object {
        var INSTANCE: LocalDatabase? = null

        fun getAppDataBase(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(LocalDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "covid19-db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}
