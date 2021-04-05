package com.example.domain.operations.characters

sealed class CharactersError {
    data class CodeWrong(val code: Int, val status: String): CharactersError()
}