package com.aryk.covid.di

import com.aryk.network.NingaApiService
import com.aryk.network.VirusTrackerApiService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    fun provideNingaService(retrofit: Retrofit): NingaApiService {
        return retrofit.create(NingaApiService::class.java)
    }

    fun provideVirusTrackerService(retrofit: Retrofit): VirusTrackerApiService {
        return retrofit.create(VirusTrackerApiService::class.java)
    }

    single { provideNingaService(get(named("ningaInstance"))) }
    single { provideVirusTrackerService(get(named("virusTrackerInstance"))) }
}
