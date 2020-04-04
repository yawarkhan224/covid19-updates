package com.aryk.covid

import android.app.Application
import com.aryk.covid.di.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
class CovidApplciation : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CovidApplciation)
            modules(listOf(repositoryModule, viewModelModule, netModule, apiModule, helperModule))
        }
    }
}
