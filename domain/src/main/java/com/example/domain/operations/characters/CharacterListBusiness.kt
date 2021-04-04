package com.example.domain.operations.characters

data class CharacterListBusiness (
    val offset: Long,
    val limit: Long,
    val total: Long,
    val count: Long,
    val results: List<CharacterBusiness>
)