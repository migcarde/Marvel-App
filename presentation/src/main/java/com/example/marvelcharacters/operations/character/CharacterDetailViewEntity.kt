package com.example.marvelcharacters.operations.character

import com.example.domain.operations.characters.CharacterListBusiness
import com.example.marvelcharacters.R
import com.example.marvelcharacters.operations.character.CharacterDetailViewContent.Companion.TYPE_CONTENT
import com.example.marvelcharacters.operations.character.CharacterDetailViewContent.Companion.TYPE_HEADER

data class CharacterDetailViewEntity(
    val id: Long,
    val name: String,
    val thumbnail: String,
    val content: List<CharacterDetailViewContent>
)

data class CharacterDetailViewContent(
    val type: Int,
    val content: String
) {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_CONTENT = 1
    }
}

fun CharacterListBusiness.toPresentationDetail(getString: (Int) -> String): CharacterDetailViewEntity {
    val character = this.results[0]
    val content = mutableListOf<CharacterDetailViewContent>()

    val thumbnail = when (character.thumbnail.path.contains("https", ignoreCase = true)) {
        true -> String.format("%s.%s", character.thumbnail.path, character.thumbnail.extension)
        false -> String.format(
            "%s.%s",
            character.thumbnail.path.replace("http", "https"),
            character.thumbnail.extension
        )
    }

    if (!character.comics.items.isNullOrEmpty()) {
        content.add(
            CharacterDetailViewContent(
                type = TYPE_HEADER,
                content = getString(R.string.comics)
            )
        )
        character.comics.items.forEach {
            content.add(
                CharacterDetailViewContent(
                    type = TYPE_CONTENT,
                    content = it.name
                )
            )
        }
    }

    if (!character.stories.items.isNullOrEmpty()) {
        content.add(
            CharacterDetailViewContent(
                type = TYPE_HEADER,
                content = getString(R.string.stories)
            )
        )
        character.stories.items.forEach {
            content.add(
                CharacterDetailViewContent(
                    type = TYPE_CONTENT,
                    content = it.name
                )
            )
        }
    }

    if (!character.series.items.isNullOrEmpty()) {
        content.add(
            CharacterDetailViewContent(
                type = TYPE_HEADER,
                content = getString(R.string.series)
            )
        )
        character.series.items.forEach {
            content.add(
                CharacterDetailViewContent(
                    type = TYPE_CONTENT,
                    content = it.name
                )
            )
        }
    }

    return CharacterDetailViewEntity(
        id = character.id,
        name = character.name,
        thumbnail = thumbnail,
        content = content
    )
}