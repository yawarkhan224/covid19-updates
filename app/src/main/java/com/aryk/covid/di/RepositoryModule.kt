package com.aryk.covid.di

import com.aryk.covid.repositories.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single {
        DataRepository(get(), get(), get())
    }
}
