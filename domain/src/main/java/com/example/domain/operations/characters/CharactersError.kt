package com.example.domain.operations.characters

sealed class CharactersError {
    data class CodeWrong(val message: String): CharactersError()
}