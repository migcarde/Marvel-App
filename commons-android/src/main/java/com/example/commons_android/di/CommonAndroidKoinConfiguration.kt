package com.example.commons_android.di

import com.example.commons_android.system.SystemInformation
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class CommonAndroidKoinConfiguration {
    fun getModule() = module {
        single { SystemInformation(androidContext()) }
    }
}