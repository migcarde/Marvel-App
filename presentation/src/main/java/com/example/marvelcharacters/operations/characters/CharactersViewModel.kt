package com.example.marvelcharacters.operations.characters

import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*
import com.example.marvelcharacters.base.BaseViewModel
import java.time.Instant
import java.time.format.DateTimeFormatter

class CharactersViewModel(private val getCharacters: GetCharacters) :
    BaseViewModel<CharactersViewState, CharactersViewTransition>() {

    val state by lazy {
        viewState.value as? CharactersViewState.Characters ?: CharactersViewState.Characters()
    }
    val stateUpdate by lazy {
        viewState.value as? CharactersViewState.CharactersForUpdate
            ?: CharactersViewState.CharactersForUpdate()
    }

    fun getCharacters(timestamp: String) {
        if (state.data == null) {
            viewState.value = state.apply { loading = true }

            getCharacters(CharactersDataInput(timestamp = timestamp, offset = 0)) {
                viewState.value = state.apply { loading = false }
                it.fold(::handleFailure, ::handleSuccess)
            }
        }
    }

    fun updateCharacters(timestamp: String) {
        stateUpdate.data?.let {
            updateData(it, timestamp)
        } ?: state.data?.let {
            updateData(it, timestamp)
        }
    }

    private fun updateData(data: CharacterListViewEntity, timestamp: String) {
        val input = CharactersDataInput(timestamp = timestamp, offset = data.offset + data.count)

        getCharacters(input) {
            it.fold(::handleFailure, ::handleUpdate)
        }
    }

    private fun handleFailure(failure: CharactersFailure) {
        viewTransition.value = when (failure) {
            is CharactersFailure.Repository -> handleRepositoryFailure(failure.error)
            is CharactersFailure.Know -> handleKnowFailures(failure.error)
            else -> CharactersViewTransition.OnUnknown
        }
    }

    private fun handleRepositoryFailure(error: RepositoryFailure): CharactersViewTransition =
        when (error) {
            is RepositoryFailure.NoInternet -> CharactersViewTransition.OnNoInternet
            else -> CharactersViewTransition.OnUnknown
        }

    private fun handleKnowFailures(know: CharactersError) = when (know) {
        is CharactersError.CodeWrong -> CharactersViewTransition.OnKnow(know.status)
        else -> CharactersViewTransition.OnUnknown
    }

    private fun handleSuccess(business: CharacterListBusiness) {
        viewState.value = state.apply {
            data = business.toPresentation()
        }
    }

    private fun handleUpdate(charactersForUpdate: CharacterListBusiness) {
        stateUpdate.data = charactersForUpdate.toPresentation()
        viewState.value = stateUpdate
    }
}