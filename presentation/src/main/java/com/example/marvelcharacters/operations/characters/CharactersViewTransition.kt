package com.example.marvelcharacters.operations.characters

sealed class CharactersViewTransition {
    // Failure
    data class OnKnow(val message: String): CharactersViewTransition()
    object OnNoInternet: CharactersViewTransition()
    object OnUnknown: CharactersViewTransition()
}