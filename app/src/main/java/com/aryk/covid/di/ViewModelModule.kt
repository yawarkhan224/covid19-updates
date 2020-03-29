package com.aryk.covid.di

import com.aryk.covid.ui.dashboard.DashboardViewModel
import com.aryk.covid.ui.home.HomeViewModel
import com.aryk.covid.ui.notifications.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        DashboardViewModel()
    }
    viewModel {
        NotificationsViewModel()
    }
}
