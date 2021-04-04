package com.example.domain.operations.characters

import com.example.commons.data.types.Either
import com.example.domain.base.BaseUseCase

class GetCharacters(private val charactersRepository: CharactersRepository) :
    BaseUseCase<CharactersFailure, CharacterListBusiness, CharactersDataInput>() {
    override suspend fun run(params: CharactersDataInput): Either<CharactersFailure, CharacterListBusiness> =
        charactersRepository.getCharacters(params)
}