package com.aryk.covid.di

import com.aryk.covid.ui.detail.DetailViewModel
import com.aryk.covid.ui.home.HomeViewModel
import com.aryk.covid.ui.notifications.NotificationsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        DetailViewModel(get())
    }
    viewModel {
        NotificationsViewModel()
    }
}
