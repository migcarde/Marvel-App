package com.example.marvelcharacters.operations.character

import com.example.marvelcharacters.operations.characters.CharactersViewTransition

sealed class CharacterViewTransition {
    // Failure
    data class OnKnow(val message: String): CharacterViewTransition()
    object OnNoInternet: CharacterViewTransition()
    object OnUnknown: CharacterViewTransition()
}