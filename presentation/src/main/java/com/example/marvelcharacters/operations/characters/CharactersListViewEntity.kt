package com.example.marvelcharacters.operations.characters

import com.example.domain.operations.characters.CharacterBusiness
import com.example.domain.operations.characters.CharacterListBusiness
import com.example.marvelcharacters.operations.characters.CharactersAdapterViewEntity.Companion.TYPE_CONTENT
import com.example.marvelcharacters.operations.characters.CharactersAdapterViewEntity.Companion.TYPE_LOADING

data class CharacterListViewEntity(
    val total: Long,
    val count: Long,
    val offset: Long,
    var results: List<CharactersAdapterViewEntity>
)

data class CharacterViewEntity(
    val id: Long,
    val name: String,
    val thumbnail: String,
    val numberOfComics: Long,
    val numberOfSeries: Long,
    val numberOfStories: Long
)

data class CharactersAdapterViewEntity(
    val type: Int,
    val content: CharacterViewEntity? = null
) {
    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_LOADING = 1
    }
}

fun CharacterListBusiness.toPresentation(): CharacterListViewEntity {
    val results = this.results.map { it.toPresentation() }.toMutableList()

    if (this.count == this.limit) {
        results.add(CharactersAdapterViewEntity(type = TYPE_LOADING))
    }

    return CharacterListViewEntity(
        total = this.total,
        count = this.count,
        offset = this.offset,
        results = results
    )
}


fun CharacterBusiness.toPresentation(): CharactersAdapterViewEntity {
    val thumbnail = when (this.thumbnail.path.contains("https", ignoreCase = true)) {
        true -> String.format("%s.%s", this.thumbnail.path, this.thumbnail.extension)
        false -> String.format(
            "%s.%s",
            this.thumbnail.path.replace("http", "https"),
            this.thumbnail.extension
        )
    }

    val content = CharacterViewEntity(
        id = this.id,
        name = this.name,
        thumbnail = thumbnail,
        numberOfComics = this.comics.returned,
        numberOfSeries = this.series.returned,
        numberOfStories = this.stories.returned
    )

    return CharactersAdapterViewEntity(
        type = TYPE_CONTENT,
        content = content
    )
}