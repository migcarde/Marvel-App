package com.example.marvelcharacters.operations.characters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class CharactersViewState : Parcelable {
    @Parcelize
    data class Characters(
        var loading: Boolean = true,
        var data: @RawValue CharacterListViewEntity? = null
    ) : CharactersViewState()

    @Parcelize
    data class CharactersForUpdate(
        var data: @RawValue CharacterListViewEntity? = null
    ) : CharactersViewState()
}