package com.example.domain.operations.characters

import com.example.domain.RepositoryFailure

sealed class CharactersFailure {

    data class Repository(val error: RepositoryFailure) : CharactersFailure()
    data class Know(val error: CharactersError) : CharactersFailure()
}