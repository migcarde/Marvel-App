package com.example.marvelcharacters.operations.character

import com.example.domain.RepositoryFailure
import com.example.domain.operations.characters.*
import com.example.marvelcharacters.base.BaseViewModel

class CharacterViewModel(private val getCharacter: GetCharacter) :
    BaseViewModel<CharacterViewState, CharacterViewTransition>() {

    val state by lazy {
        viewState.value as? CharacterViewState.Character ?: CharacterViewState.Character()
    }

    fun getCharacter(id: Long, timestamp: String, getString: (Int) -> String) {
        if (state.data == null) {
            viewState.value= state.apply { loading = true }

            getCharacter(CharacterDataInput(id = id, timestamp = timestamp)) {
                viewState.value = state.apply { loading = false }

                it.fold(left = ::handleFailure, right = { character -> handleSuccess(character, getString) })
            }
        }
    }

    private fun handleFailure(failure: CharactersFailure) {
        viewTransition.value = when (failure) {
            is CharactersFailure.Repository -> handleRepositoryFailure(failure.error)
            is CharactersFailure.Know -> handleKnowFailures(failure.error)
            else -> CharacterViewTransition.OnUnknown
        }
    }

    private fun handleRepositoryFailure(error: RepositoryFailure): CharacterViewTransition =
        when (error) {
            is RepositoryFailure.NoInternet -> CharacterViewTransition.OnNoInternet
            else -> CharacterViewTransition.OnUnknown
        }

    private fun handleKnowFailures(know: CharactersError) = when (know) {
        is CharactersError.CodeWrong -> CharacterViewTransition.OnKnow(know.status)
        else -> CharacterViewTransition.OnUnknown
    }

    private fun handleSuccess(business: CharacterListBusiness, getString: (Int) -> String) {
        viewState.value = state.apply {
            data = business.toPresentationDetail(getString)
        }
    }
}