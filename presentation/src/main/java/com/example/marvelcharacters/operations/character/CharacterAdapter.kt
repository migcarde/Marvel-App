package com.example.marvelcharacters.operations.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelcharacters.databinding.CharacterItemBodyDataBinding
import com.example.marvelcharacters.databinding.CharacterItemBodyHeaderBinding
import com.example.marvelcharacters.operations.character.CharacterDetailViewContent.Companion.TYPE_HEADER

class CharacterAdapter(val details: List<CharacterDetailViewContent>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = details[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_HEADER -> {
                val view = CharacterItemBodyHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                CharacterBodyHeaderHolder(view)
            }
            else -> {
                val view = CharacterItemBodyDataBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                CharacterBodyDataHolder(view)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterBodyHeaderHolder -> holder.bind(details[position].content)
            is CharacterBodyDataHolder -> holder.bind(details[position].content)
        }
    }

    override fun getItemCount(): Int = details.size

    class CharacterBodyHeaderHolder(private val binding: CharacterItemBodyHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) = binding.apply {
            tvTitle.text = title
        }
    }

    class CharacterBodyDataHolder(private val binding: CharacterItemBodyDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) = binding.apply {
            tvTitle.text = data
        }
    }
}