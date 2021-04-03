package com.example.commons.di

import com.example.commons.json.JsonParser
import com.example.commons.json.JsonParserImpl
import org.koin.dsl.module

class CommonsKoinConfiguration {

    fun getModule() = module {

        single<JsonParser> { JsonParserImpl() }
    }
}