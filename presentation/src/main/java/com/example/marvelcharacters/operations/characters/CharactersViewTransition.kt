package com.example.marvelcharacters.operations.characters

sealed class CharactersViewTransition {
    // Failure
    object OnNoInternet: CharactersViewTransition()
    object OnUnknown: CharactersViewTransition()
}