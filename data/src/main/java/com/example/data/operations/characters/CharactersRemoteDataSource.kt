package com.example.data.operations.characters

import com.example.data.ParsedResponse
import com.example.data.ResponseParser
import com.example.data.ResponseParser.Companion.PUBIC_CHARACTER_LIST_ERROR
import com.example.domain.operations.characters.CharactersError
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.reflect.KClass

class CharactersRemoteDataSource(
    private val charactersService: CharactersService,
    private val responseParser: ResponseParser
) {
    suspend fun getCharacters(charactersDataRequest: CharactersDataRequest): ParsedResponse<CharactersError, CharacterResponse> {
        val response: Response<ResponseBody> = charactersService.getCharacters(
            timestamp = charactersDataRequest.timestamp,
            apiKey = charactersDataRequest.apiKey,
            hash = charactersDataRequest.hash,
            offset = charactersDataRequest.offset
        ).await()

        return responseParser.parse(response, getKnowErrorClassByErrorCode())
    }

    suspend fun getCharacter(characterDataRequest: CharacterDataRequest): ParsedResponse<CharactersError, CharacterResponse> {
        val response = charactersService.getCharacter(
            timestamp = characterDataRequest.timestamp,
            apiKey = characterDataRequest.apiKey,
            hash = characterDataRequest.hash,
            id = characterDataRequest.id
        ).await()

        return responseParser.parse(response, getKnowErrorClassByErrorCode())
    }

    private fun getKnowErrorClassByErrorCode(): Map<String, KClass<out CharactersError>> = mapOf(
        PUBIC_CHARACTER_LIST_ERROR to CharactersError.CodeWrong::class
    )
}