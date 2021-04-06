package com.example.data.operations.characters

import com.example.domain.operations.characters.*

data class CharacterResponse (
    val code: Long,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: DataResponse
)

data class DataResponse (
    val offset: Long,
    val limit: Long,
    val total: Long,
    val count: Long,
    val results: List<ResultResponse>
)

data class ResultResponse (
    val id: Long,
    val name: String,
    val description: String,
    val modified: String,
    val thumbnail: ThumbnailResponse,
    val resourceURI: String,
    val comics: ComicsResponse,
    val series: ComicsResponse,
    val stories: StoriesResponse,
    val events: ComicsResponse,
    val urls: List<URLResponse>
)

data class ComicsResponse (
    val available: Long,
    val collectionURI: String,
    val items: List<ComicsItemResponse>,
    val returned: Long
)

data class ComicsItemResponse (
    val resourceURI: String,
    val name: String
)

data class StoriesResponse (
    val available: Long,
    val collectionURI: String,
    val items: List<StoriesItemResponse>,
    val returned: Long
)

data class StoriesItemResponse (
    val resourceURI: String,
    val name: String,
    val type: String
)

data class ThumbnailResponse (
    val path: String,
    val extension: String
)

data class URLResponse (
    val type: String,
    val url: String
)

fun CharacterResponse.toDomain() = CharacterListBusiness(
    offset = this.data.offset,
    limit = this.data.limit,
    total = this.data.total,
    count = this.data.count,
    results = this.data.results.map { it.toDomain() }
)

fun ResultResponse.toDomain() = CharacterBusiness(
    id = this.id,
    name = this.name,
    description = this.description,
    modified = this.modified,
    thumbnail = this.thumbnail.toDomain(),
    resourceURI = this.resourceURI,
    comics = this.comics.toDomain(),
    series = this.comics.toDomain(),
    stories = this.stories.toDomain(),
    events = this.events.toDomain(),
    urls = this.urls.map { it.toDomain() }
)

fun ThumbnailResponse.toDomain() = ThumbnailBusiness(
    path = this.path,
    extension = this.extension
)

fun ComicsResponse.toDomain() = ComicsBusiness(
    available = this.available,
    collectionURI = this.collectionURI,
    returned = this.returned,
    items = this.items.map { it.toDomain() }
)

fun ComicsItemResponse.toDomain() = ComicsItemBusiness(
    resourceURI = this.resourceURI,
    name = this.name
)

fun StoriesResponse.toDomain() = StoriesBusiness(
    available = this.available,
    collectionURI = this.collectionURI,
    returned = this.returned,
    items = this.items.map { it.toDomain() }
)

fun StoriesItemResponse.toDomain() = StoriesItemBusiness(
    resourceURI = this.resourceURI,
    name = this.name,
    type = this.type
)

fun URLResponse.toDomain() = URLBusiness(
    type = this.type,
    url = this.url
)