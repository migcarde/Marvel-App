package com.example.data.operations.characters

import com.example.commons.data.types.Either
import com.example.commons_android.system.SystemInformation
import com.example.data.ParsedResponse
import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CharactersRepositoryImplTest {

    private val charactersRemoteDataSource = mock(CharactersRemoteDataSource::class.java)
    private val systemInformation = mock(SystemInformation::class.java)

    private val charactersRepositoryImpl =
        CharactersRepositoryImpl(charactersRemoteDataSource, systemInformation)

    val result = ResultResponse(
        id = 1,
        name = "Test",
        description = "Test",
        modified = "Test",
        thumbnail = ThumbnailResponse(
            path = "Test",
            extension = "Test"
        ),
        resourceURI = "Test",
        comics = ComicsResponse(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                ComicsItemResponse(
                    resourceURI = "Test",
                    name = "Test"
                )
            )
        ),
        series = ComicsResponse(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                ComicsItemResponse(
                    resourceURI = "Test",
                    name = "Test"
                )
            )
        ),
        events = ComicsResponse(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                ComicsItemResponse(
                    resourceURI = "Test",
                    name = "Test"
                )
            )
        ),
        stories = StoriesResponse(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                StoriesItemResponse(
                    resourceURI = "Test",
                    name = "Test",
                    type = "Test"
                )
            )
        ),
        urls = listOf(
            URLResponse(type = "Test", url = "Test")
        )
    )

    val charactersResponse = CharacterResponse(
        code = 1,
        status = "Test",
        copyright = "Test",
        attributionText = "Test",
        attributionHTML = "Test",
        etag = "Test",
        data = DataResponse(
            offset = 1,
            limit = 1,
            total = 1,
            count = 1,
            results = listOf(result)
        )
    )

    val resultBusiness = CharacterBusiness(
        id = 1,
        name = "Test",
        description = "Test",
        modified = "Test",
        thumbnail = ThumbnailBusiness(
            path = "Test",
            extension = "Test"
        ),
        resourceURI = "Test",
        comics = ComicsBusiness(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                ComicsItemBusiness(
                    resourceURI = "Test",
                    name = "Test"
                )
            )
        ),
        series = ComicsBusiness(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                ComicsItemBusiness(
                    resourceURI = "Test",
                    name = "Test"
                )
            )
        ),
        events = ComicsBusiness(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                ComicsItemBusiness(
                    resourceURI = "Test",
                    name = "Test"
                )
            )
        ),
        stories = StoriesBusiness(
            available = 1,
            collectionURI = "Test",
            returned = 1,
            items = listOf(
                StoriesItemBusiness(
                    resourceURI = "Test",
                    name = "Test",
                    type = "Test"
                )
            )
        ),
        urls = listOf(
            URLBusiness(type = "Test", url = "Test")
        )
    )

    val charactersBusiness = CharacterListBusiness(
        offset = 1,
        limit = 1,
        total = 1,
        count = 1,
        results = listOf(resultBusiness)
    )

    val input = CharactersDataInput(timestamp = "test", offset = 0)
    val inputCharacter = CharacterDataInput(timestamp = "test", id = 1)

    @Test
    fun `Get characters - Success`() = runBlocking {
        // Given
        val response = ParsedResponse.Success(charactersResponse)
        val request = input.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacters(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacters(input)

        // Then
        val expectedResult = Either.Right(charactersBusiness)

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get characters - Know error`() = runBlocking {
        // Given
        val response = ParsedResponse.KnownError(CharactersError.CodeWrong(500, "Test"))
        val request = input.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacters(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacters(input)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Know(CharactersError.CodeWrong(500, "Test")))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get characters - Unknown`() = runBlocking {
        // Given
        val response = ParsedResponse.Failure(RepositoryFailure.Unknown)
        val request = input.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacters(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacters(input)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unknown))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get characters - Unauthorized`() = runBlocking {
        // Given
        val response = ParsedResponse.Failure(RepositoryFailure.Unauthorized)
        val request = input.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacters(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacters(input)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unauthorized))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get characters - Server error`() = runBlocking {
        // Given
        val response = ParsedResponse.Failure(RepositoryFailure.ServerError)
        val request = input.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacters(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacters(input)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.ServerError))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get character - Success`() = runBlocking {
        // Given
        val response = ParsedResponse.Success(charactersResponse)
        val request = inputCharacter.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacter(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacter(inputCharacter)

        // Then
        val expectedResult = Either.Right(charactersBusiness)

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get character - Know error`() = runBlocking {
        // Given
        val response = ParsedResponse.KnownError(CharactersError.CodeWrong(500, "Test"))
        val request = inputCharacter.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacter(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacter(inputCharacter)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Know(CharactersError.CodeWrong(500, "Test")))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get character - Unknown`() = runBlocking {
        // Given
        val response = ParsedResponse.Failure(RepositoryFailure.Unknown)
        val request = inputCharacter.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacter(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacter(inputCharacter)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unknown))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get character - Unauthorized`() = runBlocking {
        // Given
        val response = ParsedResponse.Failure(RepositoryFailure.Unauthorized)
        val request = inputCharacter.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacter(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacter(inputCharacter)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unauthorized))

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get character - Server error`() = runBlocking {
        // Given
        val response = ParsedResponse.Failure(RepositoryFailure.ServerError)
        val request = inputCharacter.toRequest()

        `when`(systemInformation.hasConnection).thenReturn(true)
        `when`(charactersRemoteDataSource.getCharacter(request)).thenReturn(response)

        // When
        val result = charactersRepositoryImpl.getCharacter(inputCharacter)

        // Then
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.ServerError))

        Assert.assertEquals(expectedResult, result)
    }
}