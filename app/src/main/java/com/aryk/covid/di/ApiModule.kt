package com.aryk.covid.di

import com.aryk.network.Covid19Service
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    fun provideCovidService(retrofit: Retrofit): Covid19Service {
        return retrofit.create(Covid19Service::class.java)
    }

    single { provideCovidService(get()) }
}
