package operations

import com.example.commons.data.types.Either
import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class GetCharactersTest {

    private val charactersRepository = mock(CharactersRepository::class.java)

    private val getCharacters = GetCharacters(charactersRepository)

    private val input = CharactersDataInput(timestamp = "test")

    @Test
    fun `Get Characters - Success`() = runBlocking {
        // Given
        val characters = mock(CharacterListBusiness::class.java)
        val expectedResult = Either.Right(characters)

        `when`(charactersRepository.getCharacters(input)).thenReturn(expectedResult)

        // When
        val result = getCharacters.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }
    @Test
    fun `Get Characters - Know error`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Know(CharactersError.CodeWrong("Test")))

        `when`(charactersRepository.getCharacters(input)).thenReturn(expectedResult)

        // When
        val result = getCharacters.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }


    @Test
    fun `Get Characters - Server error`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.ServerError))

        `when`(charactersRepository.getCharacters(input)).thenReturn(expectedResult)

        // When
        val result = getCharacters.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get Characters - Unknown`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unknown))

        `when`(charactersRepository.getCharacters(input)).thenReturn(expectedResult)

        // When
        val result = getCharacters.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get Characters - No internet`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.NoInternet))

        `when`(charactersRepository.getCharacters(input)).thenReturn(expectedResult)

        // When
        val result = getCharacters.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get Characters - Unauthorized`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unauthorized))

        `when`(charactersRepository.getCharacters(input)).thenReturn(expectedResult)

        // When
        val result = getCharacters.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }
}