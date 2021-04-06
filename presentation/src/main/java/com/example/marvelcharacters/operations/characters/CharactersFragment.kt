package com.example.marvelcharacters.operations.characters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.marvelcharacters.R
import com.example.marvelcharacters.base.BaseFragment
import com.example.marvelcharacters.databinding.CharactersFragmentBinding
import com.example.marvelcharacters.operations.characters.CharactersAdapterViewEntity.Companion.TYPE_LOADING
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.format.DateTimeFormatter

class CharactersFragment : BaseFragment<CharactersViewState, CharactersViewTransition>() {
    override val viewModel by viewModel<CharactersViewModel>()
    private val binding get() = _binding as CharactersFragmentBinding
    private lateinit var charactersAdapter: CharactersAdapter

    override fun getViewBinding(container: ViewGroup?): ViewBinding =
        CharactersFragmentBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        viewModel.getCharacters(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
    }

    override fun initListeners() = Unit // TODO: Implement it

    override fun manageState(state: CharactersViewState) {
        when (state) {
            is CharactersViewState.Characters -> {
                binding.pbLoading.visibility = when (state.loading) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }

                state.data?.results?.let {
                    if (it.isNotEmpty()) {
                        binding.rvBooks.visibility = View.VISIBLE
                        setRecyclerView(it)
                    }
                }
            }
            is CharactersViewState.CharactersForUpdate -> {
                state.data?.results?.let {
                    if (it.isNotEmpty()) {
                        updateRecyclerView(it)
                    }
                }
            }
        }
    }

    private fun updateRecyclerView(characters: List<CharactersAdapterViewEntity>) {
        val initialSize = charactersAdapter.characters.size

        charactersAdapter.characters.apply {
            removeLast()
            addAll(characters)
        }

        binding.rvBooks.adapter?.notifyItemRangeInserted(
            initialSize,
            charactersAdapter.characters.size
        )
    }

    private fun setRecyclerView(characters: List<CharactersAdapterViewEntity>) {
        binding.rvBooks.apply {
            setHasFixedSize(true)
            val layout = LinearLayoutManager(requireContext())
            layoutManager = layout
            charactersAdapter = CharactersAdapter(
                characters.toMutableList(),
                onClick = { viewModel.goToDetail(it) })
            adapter = charactersAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (layout.findLastCompletelyVisibleItemPosition() == charactersAdapter.itemCount - 1 && charactersAdapter.characters.last().type == TYPE_LOADING) {
                        viewModel.updateCharacters(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                    }
                }
            })
        }
    }

    override fun manageTransition(transition: CharactersViewTransition) {
        when (transition) {
            // Success
            is CharactersViewTransition.GoToDetail -> findNavController().navigate(
                CharactersFragmentDirections.actionCharactersFragmentToCharacterFragment(transition.id)
            )

            // Failure
            is CharactersViewTransition.OnUnknown -> binding.tvMessage.apply {
                text = getString(R.string.unknown_error)
                visibility = View.VISIBLE
            }

            is CharactersViewTransition.OnNoInternet -> binding.tvMessage.apply {
                text = getString(R.string.no_internet_error)
                visibility = View.VISIBLE
            }

            is CharactersViewTransition.OnKnow -> binding.tvMessage.apply {
                text = transition.message
                visibility = View.VISIBLE
            }
        }
    }

}