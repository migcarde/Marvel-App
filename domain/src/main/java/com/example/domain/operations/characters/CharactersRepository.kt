package com.example.domain.operations.characters

import com.example.commons.data.types.Either

interface CharactersRepository {
    suspend fun getCharacters(charactersDataInput: CharactersDataInput): Either<CharactersFailure, CharacterListBusiness>
}