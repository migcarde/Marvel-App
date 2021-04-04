package com.example.domain.operations.characters




data class CharacterBusiness (
    val id: Long,
    val name: String,
    val description: String,
    val modified: String,
    val thumbnail: ThumbnailBusiness,
    val resourceURI: String,
    val comics: ComicsBusiness,
    val series: ComicsBusiness,
    val stories: StoriesBusiness,
    val events: ComicsBusiness,
    val urls: List<URLBusiness>
)

data class ComicsBusiness (
    val available: Long,
    val collectionURI: String,
    val items: List<ComicsItemBusiness>,
    val returned: Long
)

data class ComicsItemBusiness (
    val resourceURI: String,
    val name: String
)

data class StoriesBusiness (
    val available: Long,
    val collectionURI: String,
    val items: List<StoriesItemBusiness>,
    val returned: Long
)

data class StoriesItemBusiness (
    val resourceURI: String,
    val name: String,
    val type: String
)

data class ThumbnailBusiness (
    val path: String,
    val extension: String
)

data class URLBusiness (
    val type: String,
    val url: String
)