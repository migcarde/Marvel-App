package com.example.marvelcharacters.di

import com.example.marvelcharacters.operations.character.CharacterViewModel
import com.example.marvelcharacters.operations.characters.CharactersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class PresentationKoinConfiguration {
    fun getModule() = module {
        viewModel { CharactersViewModel(get()) }
        viewModel { CharacterViewModel(get()) }
    }
}