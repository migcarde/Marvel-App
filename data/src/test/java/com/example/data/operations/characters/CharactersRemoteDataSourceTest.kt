package com.example.data.operations.characters

import com.example.commons.json.JsonParser
import com.example.commons_android.system.SystemInformation
import com.example.data.ErrorResponse
import com.example.data.ParsedResponse
import com.example.data.ResponseParser
import com.example.domain.RepositoryFailure
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.EOFException

class CharactersRemoteDataSourceTest {

    private val charactersService = mock(CharactersService::class.java)
    private val jsonParser = mock(JsonParser::class.java)
    private val systemInformation = mock(SystemInformation::class.java)

    private val responseParser = ResponseParser(jsonParser)
    private val charactersRemoteDataSource =
        CharactersRemoteDataSource(charactersService, responseParser)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Get characters - Succcess`() = runBlocking {
        // Given
        val json = "test"
        val request = CharactersDataRequest(
            timestamp = "test",
            apiKey = "test api key",
            hash = "test hash",
            offset = 0
        )
        val mockResponse = mock(CharacterResponse::class.java)
        val responseBody = mock(ResponseBody::class.java)
        val response = mock(Response::class.java) as Response<ResponseBody>

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(responseBody.string()).thenReturn(json)
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(responseBody)
        `when`(
            charactersService.getCharacters(
                timestamp = request.timestamp,
                apiKey = request.apiKey,
                hash = request.hash,
                offset = 0
            )
        ).thenReturn(
            CompletableDeferred(response)
        )
        `when`(jsonParser.fromJson(json, CharacterResponse::class.java)).thenReturn(mockResponse)

        val responseParsed = ParsedResponse.Success(mockResponse)

        // When
        val result = charactersRemoteDataSource.getCharacters(request)

        // Then
        Assert.assertEquals(responseParsed, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Get characters - Server error`() = runBlocking {
        // Given
        val json = "test"
        val request = CharactersDataRequest(
            timestamp = "test",
            apiKey = "test api key",
            hash = "test hash",
            offset = 0
        )
        val bookResponse = mock(CharacterResponse::class.java)
        val responseBody = mock(ResponseBody::class.java)
        val response: Response<ResponseBody> = mock(Response::class.java) as Response<ResponseBody>

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(responseBody.string())
            .thenReturn(json).thenThrow(EOFException("End of input"))
        `when`(response.isSuccessful).thenReturn(false)
        `when`(response.errorBody()).thenReturn(responseBody)
        `when`(
            charactersService.getCharacters(
                timestamp = request.timestamp,
                apiKey = request.apiKey,
                hash = request.hash,
                offset = 0
            )
        ).thenReturn(CompletableDeferred(response))
        `when`(jsonParser.fromJson(json, ErrorResponse::class.java)).thenReturn(
            ErrorResponse(
                code = 500,
                status = "Test",
            )
        )

        val responseParsed = ParsedResponse.Failure(RepositoryFailure.ServerError)

        // When
        val result = charactersRemoteDataSource.getCharacters(request)

        // Then
        Assert.assertEquals(responseParsed, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Get character - Succcess`() = runBlocking {
        // Given
        val json = "test"
        val request = CharacterDataRequest(
            timestamp = "test",
            apiKey = "test api key",
            hash = "test hash",
            id = 1
        )
        val mockResponse = mock(CharacterResponse::class.java)
        val responseBody = mock(ResponseBody::class.java)
        val response = mock(Response::class.java) as Response<ResponseBody>

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(responseBody.string()).thenReturn(json)
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(responseBody)
        `when`(
            charactersService.getCharacter(
                timestamp = request.timestamp,
                apiKey = request.apiKey,
                hash = request.hash,
                id = 1
            )
        ).thenReturn(
            CompletableDeferred(response)
        )
        `when`(jsonParser.fromJson(json, CharacterResponse::class.java)).thenReturn(mockResponse)

        val responseParsed = ParsedResponse.Success(mockResponse)

        // When
        val result = charactersRemoteDataSource.getCharacter(request)

        // Then
        Assert.assertEquals(responseParsed, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Get character - Server error`() = runBlocking {
        // Given
        val json = "test"
        val request = CharacterDataRequest(
            timestamp = "test",
            apiKey = "test api key",
            hash = "test hash",
            id = 1
        )
        val bookResponse = mock(CharacterResponse::class.java)
        val responseBody = mock(ResponseBody::class.java)
        val response: Response<ResponseBody> = mock(Response::class.java) as Response<ResponseBody>

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(responseBody.string())
            .thenReturn(json).thenThrow(EOFException("End of input"))
        `when`(response.isSuccessful).thenReturn(false)
        `when`(response.errorBody()).thenReturn(responseBody)
        `when`(
            charactersService.getCharacter(
                timestamp = request.timestamp,
                apiKey = request.apiKey,
                hash = request.hash,
                id = 1
            )
        ).thenReturn(CompletableDeferred(response))
        `when`(jsonParser.fromJson(json, ErrorResponse::class.java)).thenReturn(
            ErrorResponse(
                code = 500,
                status = "Test",
            )
        )

        val responseParsed = ParsedResponse.Failure(RepositoryFailure.ServerError)

        // When
        val result = charactersRemoteDataSource.getCharacter(request)

        // Then
        Assert.assertEquals(responseParsed, result)
    }
}