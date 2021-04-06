package com.example.marvelcharacters.operations.characters

sealed class CharactersViewTransition {
    // Success
    data class GoToDetail(val id: Long): CharactersViewTransition()

    // Failure
    data class OnKnow(val message: String): CharactersViewTransition()
    object OnNoInternet: CharactersViewTransition()
    object OnUnknown: CharactersViewTransition()
}