package com.aryk.covid.di

import com.aryk.network.Covid19Service
import com.aryk.network.VirusTrackerService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    fun provideNingaService(retrofit: Retrofit): Covid19Service {
        return retrofit.create(Covid19Service::class.java)
    }

    fun provideVirusTrackerService(retrofit: Retrofit): VirusTrackerService {
        return retrofit.create(VirusTrackerService::class.java)
    }

    single { provideNingaService(get(named("ningaInstance"))) }
    single { provideVirusTrackerService(get(named("virusTrackerInstance"))) }
}
