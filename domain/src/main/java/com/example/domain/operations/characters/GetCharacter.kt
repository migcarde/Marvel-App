package com.example.domain.operations.characters

import com.example.commons.data.types.Either
import com.example.domain.base.BaseUseCase

class GetCharacter(private val charactersRepository: CharactersRepository) :
    BaseUseCase<CharactersFailure, CharacterListBusiness, CharacterDataInput>() {
    override suspend fun run(params: CharacterDataInput): Either<CharactersFailure, CharacterListBusiness> =
        charactersRepository.getCharacter(params)
}