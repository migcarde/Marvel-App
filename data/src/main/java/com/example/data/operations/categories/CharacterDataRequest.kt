package com.example.data.operations.categories

import com.example.commons.hash.toMD5Hash
import com.example.data.BuildConfig
import com.example.data.operations.base.BaseDataRequest
import com.example.domain.operations.characters.CharactersDataInput

data class CharacterDataRequest(
    override val timestamp: String,
    override val apiKey: String,
    override val hash: String,
    val offset: Long
) : BaseDataRequest()

fun CharactersDataInput.toRequest(): CharacterDataRequest {
    val hash = this.timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY

    return CharacterDataRequest(
        timestamp = this.timestamp,
        apiKey = BuildConfig.PUBLIC_KEY,
        hash = hash.toMD5Hash(),
        offset = this.offset
    )
}