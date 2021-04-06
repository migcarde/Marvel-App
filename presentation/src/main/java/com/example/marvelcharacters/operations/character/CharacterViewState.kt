package com.example.marvelcharacters.operations.character

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class CharacterViewState : Parcelable {

    @Parcelize
    data class Character(
        var loading: Boolean = true,
        var data: @RawValue CharacterDetailViewEntity? = null
    ) : CharacterViewState()
}