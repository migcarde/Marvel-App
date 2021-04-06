package com.example.marvelcharacters.operations.character

import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import coil.api.load
import com.example.marvelcharacters.R
import com.example.marvelcharacters.base.BaseFragment
import com.example.marvelcharacters.databinding.CharacterFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.format.DateTimeFormatter

class CharacterFragment : BaseFragment<CharacterViewState, CharacterViewTransition>() {
    override val viewModel by viewModel<CharacterViewModel>()
    private val binding get() = _binding as CharacterFragmentBinding
    private val args: CharacterFragmentArgs by navArgs()
    private lateinit var characterAdapter: CharacterAdapter

    override fun getViewBinding(container: ViewGroup?): ViewBinding =
        CharacterFragmentBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        viewModel.getCharacter(
            id = args.id,
            timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            getString = { getStringResource(it) }
        )
    }

    private fun getStringResource(id: Int) = getString(id)

    override fun initListeners() = Unit // Not implemented

    override fun manageState(state: CharacterViewState) {
        when (state) {
            is CharacterViewState.Character -> {
                binding.pbLoading.visibility = when (state.loading) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }

                state.data?.let {
                    binding.apply {
                        ivPicture.apply {
                            load(it.thumbnail)
                            visibility = View.VISIBLE
                        }

                        tvName.apply {
                            text = it.name
                            visibility = View.VISIBLE
                        }

                        setRecyclerView(it.content)
                    }
                }
            }
        }
    }

    private fun setRecyclerView(content: List<CharacterDetailViewContent>) {
        binding.rvBooks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            characterAdapter = CharacterAdapter(content)
            adapter = characterAdapter
        }
    }

    override fun manageTransition(transition: CharacterViewTransition) {
        when (transition) {
            is CharacterViewTransition.OnUnknown -> binding.tvMessage.apply {
                text = getString(R.string.unknown_error)
                visibility = View.VISIBLE
            }
            is CharacterViewTransition.OnNoInternet -> binding.tvMessage.apply {
                text = getString(R.string.no_internet_error)
                visibility = View.VISIBLE
            }

            is CharacterViewTransition.OnKnow -> binding.tvMessage.apply {
                text = transition.message
                visibility = View.VISIBLE
            }
        }
    }
}