package com.example.marvelcharacters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.commons.data.types.Either
import com.example.commons.mockito.any
import com.example.commons.mockito.eq
import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*
import com.example.marvelcharacters.operations.characters.*
import com.example.marvelcharacters.operations.characters.CharactersAdapterViewEntity.Companion.TYPE_CONTENT
import com.example.marvelcharacters.operations.characters.CharactersAdapterViewEntity.Companion.TYPE_LOADING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.Instant
import java.time.format.DateTimeFormatter

class CharactersViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // View model parameters
    private val state = Mockito.mock(Observer::class.java as Class<Observer<CharactersViewState>>)
    private val transition =
        Mockito.mock(Observer::class.java as Class<Observer<CharactersViewTransition>>)
    private val getCharacters = Mockito.mock(GetCharacters::class.java)

    private val viewModel = CharactersViewModel(getCharacters)

    // Example values for testing
    val business = CharacterBusiness(
        id = 1,
        name = "Test",
        description = "Test",
        modified = "Test",
        thumbnail = ThumbnailBusiness(
            path = "https://www.example",
            extension = "org"
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
        results = listOf(business)
    )

    val charactersAdapterContent = CharactersAdapterViewEntity(
        type = TYPE_CONTENT,
        content = CharacterViewEntity(
            id = 1L,
            name = "Test",
            thumbnail = "https://www.example.org",
            numberOfStories = 1,
            numberOfSeries = 1,
            numberOfComics = 1
        )
    )

    val charactersAdapterLoading = CharactersAdapterViewEntity(
        type = TYPE_LOADING
    )

    val expectedResult = CharacterListViewEntity(
        offset = 1,
        total = 1,
        count = 1,
        results = listOf(charactersAdapterContent, charactersAdapterLoading)
    )

    val input = CharactersDataInput(timestamp = "test", offset = 0)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Default)
    }

    @After
    fun tearDown() {
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun `Get Characters - Success`() = runBlocking {
        // Given
        viewModel.getState().observeForever(state)

        val successValue = Either.Right(charactersBusiness)

        val argumentCaptor = ArgumentCaptor.forClass(CharactersViewState.Characters::class.java)

        Mockito.`when`(getCharacters.invoke(eq(input), any())).thenAnswer {
            it.getArgument<(Either<CharactersFailure, CharacterListBusiness>) -> Unit>(1)
                .invoke(successValue)
        }
        Mockito.`when`(getCharacters.run(input)).thenReturn(successValue)

        // When
        viewModel.getCharacters(input.timestamp)

        // Then
        Mockito.verify(state, Mockito.times(3)).onChanged(argumentCaptor.capture())

        val currentState = argumentCaptor.allValues[0]

        Assert.assertEquals(
            CharactersViewState.Characters(loading = false, data = expectedResult),
            currentState
        )
    }

    @Test
    fun `Get characters - No internet`() = runBlocking {
        // Given
        viewModel.getTransition().observeForever(transition)

        val failure = Either.Left(CharactersFailure.Repository(RepositoryFailure.NoInternet))

        val argumentCaptor = ArgumentCaptor.forClass(CharactersViewTransition.OnNoInternet::class.java)

        Mockito.`when`(getCharacters.invoke(eq(input), any())).thenAnswer {
            it.getArgument<(Either<CharactersFailure, CharacterListBusiness>) -> Unit>(1)
                .invoke(failure)
        }
        Mockito.`when`(getCharacters.run(input)).thenReturn(failure)

        // When
        viewModel.getCharacters(input.timestamp)

        // Then
        Mockito.verify(transition).onChanged(argumentCaptor.capture())

        val currentState = argumentCaptor.allValues[0]

        Assert.assertEquals(
            CharactersViewTransition.OnNoInternet,
            currentState
        )
    }

    @Test
    fun `Get characters - Unknown`() = runBlocking {
        // Given
        viewModel.getTransition().observeForever(transition)

        val failure = Either.Left(CharactersFailure.Repository(RepositoryFailure.Unknown))

        val argumentCaptor = ArgumentCaptor.forClass(CharactersViewTransition.OnUnknown::class.java)

        Mockito.`when`(getCharacters.invoke(eq(input), any())).thenAnswer {
            it.getArgument<(Either<CharactersFailure, CharacterListBusiness>) -> Unit>(1)
                .invoke(failure)
        }
        Mockito.`when`(getCharacters.run(input)).thenReturn(failure)

        // When
        viewModel.getCharacters(input.timestamp)

        // Then
        Mockito.verify(transition).onChanged(argumentCaptor.capture())

        val currentState = argumentCaptor.allValues[0]

        Assert.assertEquals(
            CharactersViewTransition.OnUnknown,
            currentState
        )
    }

    @Test
    fun `Get characters - Know error`() = runBlocking {
        // Given
        viewModel.getTransition().observeForever(transition)

        val failure = Either.Left(CharactersFailure.Know(error = CharactersError.CodeWrong(500, "Test")))

        val argumentCaptor = ArgumentCaptor.forClass(CharactersViewTransition.OnKnow("Test")::class.java)

        Mockito.`when`(getCharacters.invoke(eq(input), any())).thenAnswer {
            it.getArgument<(Either<CharactersFailure, CharacterListBusiness>) -> Unit>(1)
                .invoke(failure)
        }
        Mockito.`when`(getCharacters.run(input)).thenReturn(failure)

        // When
        viewModel.getCharacters(input.timestamp)

        // Then
        Mockito.verify(transition).onChanged(argumentCaptor.capture())

        val currentState = argumentCaptor.allValues[0]

        Assert.assertEquals(
            CharactersViewTransition.OnKnow("Test"),
            currentState
        )
    }
}