package com.example.marvelcharacters

import android.app.Application
import com.example.commons.di.CommonsKoinConfiguration
import com.example.commons_android.di.CommonAndroidKoinConfiguration
import com.example.data.di.DataKoinConfiguration
import com.example.domain.di.DomainKoinConfiguration
import com.example.marvelcharacters.di.PresentationKoinConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarvelApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MarvelApp)
            modules(listOf(
                CommonsKoinConfiguration().getModule(),
                CommonAndroidKoinConfiguration().getModule(),
                DataKoinConfiguration(BuildConfig.BASE_URL).getModule(),
                DomainKoinConfiguration().getModule(),
                PresentationKoinConfiguration().getModule()
            ))
        }
    }
}