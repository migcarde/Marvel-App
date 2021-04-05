package com.example.data.operations.categories

import com.example.data.ParsedResponse
import com.example.data.ResponseParser
import com.example.data.ResponseParser.Companion.PUBIC_CHARACTER_LIST_ERROR
import com.example.domain.operations.characters.CharactersError
import com.example.domain.operations.characters.CharactersFailure
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.reflect.KClass

class CharactersRemoteDataSource(
    private val charactersService: CharactersService,
    private val responseParser: ResponseParser
) {
    suspend fun getCharacters(characterDataRequest: CharacterDataRequest): ParsedResponse<CharactersError, CharacterResponse> {
        val response: Response<ResponseBody> = charactersService.getCharacters(
            timestamp = characterDataRequest.timestamp,
            apiKey = characterDataRequest.apiKey,
            hash = characterDataRequest.hash,
             offset = characterDataRequest.offset
        ).await()

        return responseParser.parse(response, getKnowErrorClassByErrorCode())
    }

    private fun getKnowErrorClassByErrorCode(): Map<String, KClass<out CharactersError>> = mapOf(
        PUBIC_CHARACTER_LIST_ERROR to CharactersError.CodeWrong::class
    )
}