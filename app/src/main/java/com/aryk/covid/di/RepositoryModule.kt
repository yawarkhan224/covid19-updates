package com.aryk.covid.di

import com.aryk.covid.repositories.DataRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        DataRepository(get(), get())
    }
}
