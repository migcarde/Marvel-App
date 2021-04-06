package com.example.data.operations.characters

import com.example.commons.data.types.Either
import com.example.commons_android.system.SystemInformation
import com.example.data.ParsedResponse
import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*

class CharactersRepositoryImpl(
    private val charactersRemoteDataSource: CharactersRemoteDataSource,
    private val systemInformation: SystemInformation
) : CharactersRepository {
    override suspend fun getCharacters(charactersDataInput: CharactersDataInput): Either<CharactersFailure, CharacterListBusiness> {
        return when (systemInformation.hasConnection) {
            true -> when (val response =
                charactersRemoteDataSource.getCharacters(charactersDataInput.toRequest())) {
                is ParsedResponse.Success -> Either.Right(response.success.toDomain())
                is ParsedResponse.KnownError -> Either.Left(CharactersFailure.Know(response.knownError))
                is ParsedResponse.Failure -> Either.Left(
                    CharactersFailure.Repository(
                        response.failure
                    )
                )
            }
            false -> Either.Left(CharactersFailure.Repository(RepositoryFailure.NoInternet))
        }
    }

    override suspend fun getCharacter(characterDataInput: CharacterDataInput): Either<CharactersFailure, CharacterListBusiness> {
        return when (systemInformation.hasConnection) {
            true -> when (val response =
                charactersRemoteDataSource.getCharacter(characterDataInput.toRequest())) {
                is ParsedResponse.Success -> Either.Right(response.success.toDomain())
                is ParsedResponse.KnownError -> Either.Left(CharactersFailure.Know(response.knownError))
                is ParsedResponse.Failure -> Either.Left(CharactersFailure.Repository(response.failure))
            }
            false -> Either.Left(CharactersFailure.Repository(RepositoryFailure.NoInternet))
        }
    }
}