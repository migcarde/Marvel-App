package operations

import com.example.commons.data.types.Either
import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class GetCharacterTest {

    private val charactersRepository = Mockito.mock(CharactersRepository::class.java)

    private val getCharacter = GetCharacter(charactersRepository)

    private val input = CharacterDataInput(timestamp = "test", id = 1)

    @Test
    fun `Get Characters - Success`() = runBlocking {
        // Given
        val characters = Mockito.mock(CharacterListBusiness::class.java)
        val expectedResult = Either.Right(characters)

        Mockito.`when`(charactersRepository.getCharacter(input)).thenReturn(expectedResult)

        // When
        val result = getCharacter.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }
    @Test
    fun `Get Characters - Know error`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Know(CharactersError.CodeWrong(500, "Test")))

        Mockito.`when`(charactersRepository.getCharacter(input)).thenReturn(expectedResult)

        // When
        val result = getCharacter.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }


    @Test
    fun `Get Characters - Server error`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.ServerError))

        Mockito.`when`(charactersRepository.getCharacter(input)).thenReturn(expectedResult)

        // When
        val result = getCharacter.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get Characters - Unknown`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unknown))

        Mockito.`when`(charactersRepository.getCharacter(input)).thenReturn(expectedResult)

        // When
        val result = getCharacter.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get Characters - No internet`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.NoInternet))

        Mockito.`when`(charactersRepository.getCharacter(input)).thenReturn(expectedResult)

        // When
        val result = getCharacter.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `Get Characters - Unauthorized`() = runBlocking {
        // Given
        val expectedResult = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unauthorized))

        Mockito.`when`(charactersRepository.getCharacter(input)).thenReturn(expectedResult)

        // When
        val result = getCharacter.run(input)

        // Then
        Assert.assertEquals(expectedResult, result)
    }
}