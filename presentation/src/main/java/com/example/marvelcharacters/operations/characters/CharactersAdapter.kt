package com.example.marvelcharacters.operations.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.marvelcharacters.databinding.CharactersItemFragmentBinding
import com.example.marvelcharacters.databinding.LoadingItemBinding
import com.example.marvelcharacters.operations.characters.CharactersAdapterViewEntity.Companion.TYPE_CONTENT

class CharactersAdapter(val characters: MutableList<CharactersAdapterViewEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_CONTENT -> {
                val view = CharactersItemFragmentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                CharactersHolder(view)
            }
            else -> {
                val view =
                    LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                LoadingHolder(view)
            }
        }

    override fun getItemViewType(position: Int): Int = characters[position].type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharactersHolder -> holder.bind(characters[position].content!!)
        }
    }

    override fun getItemCount(): Int = characters.size

    class CharactersHolder(private val binding: CharactersItemFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CharacterViewEntity) = binding.apply {
            ivPicture.load(item.thumbnail)
            tvName.text = item.name
            tvNumberOfComics.text = item.numberOfComics.toString()
            tvNumberOfStories.text = item.numberOfSeries.toString()
            tvNumberOfSeries.text = item.numberOfStories.toString()
        }
    }

    class LoadingHolder(binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}