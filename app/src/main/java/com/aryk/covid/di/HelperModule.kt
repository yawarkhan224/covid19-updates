package com.aryk.covid.di

import com.aryk.covid.helper.DeviceHelper
import com.aryk.covid.helper.TimeHelper
import org.koin.dsl.module
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

val helperModule = module {
    fun provideCalender(): Calendar {
        return Calendar.getInstance()
    }

    fun provideSystemTimeFormatter(): DateFormat {
        return SimpleDateFormat.getTimeInstance(
            SimpleDateFormat.SHORT
        )
    }

    fun provideTimeHelper(
        systemTimeFormatter: DateFormat
    ): TimeHelper {
        return TimeHelper(systemTimeFormatter)
    }

    fun provideDeviceHelper(): DeviceHelper {
        return DeviceHelper()
    }

    single { provideCalender() }
    single { provideSystemTimeFormatter() }
    single { provideTimeHelper(get()) }
    single { provideDeviceHelper() }
}
