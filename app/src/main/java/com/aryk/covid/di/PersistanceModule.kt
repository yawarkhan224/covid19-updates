package com.aryk.covid.di

import androidx.room.Room
import com.aryk.covid.persistance.LocalDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val persistanceModule = module {
    // Room Database
    single {
        Room.databaseBuilder(androidApplication(), LocalDatabase::class.java, "covid19-db")
            .build()
    }

    // Country DAO
    single { get<LocalDatabase>().countryDataDao() }
}
