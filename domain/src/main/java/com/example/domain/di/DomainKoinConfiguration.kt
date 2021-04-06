package com.example.domain.di

import com.example.domain.operations.characters.GetCharacter
import com.example.domain.operations.characters.GetCharacters
import org.koin.dsl.module

class DomainKoinConfiguration {
    fun getModule() = module {
        factory { GetCharacters(get()) }
        factory { GetCharacter(get()) }
    }
}