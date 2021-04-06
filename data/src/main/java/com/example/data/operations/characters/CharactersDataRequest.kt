package com.example.data.operations.characters

import com.example.commons.hash.toMD5Hash
import com.example.data.BuildConfig
import com.example.data.operations.base.BaseDataRequest
import com.example.domain.operations.characters.CharactersDataInput

data class CharactersDataRequest(
    override val timestamp: String,
    override val apiKey: String,
    override val hash: String,
    val offset: Long
) : BaseDataRequest()

fun CharactersDataInput.toRequest(): CharactersDataRequest {
    val hash = this.timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY

    return CharactersDataRequest(
        timestamp = this.timestamp,
        apiKey = BuildConfig.PUBLIC_KEY,
        hash = hash.toMD5Hash(),
        offset = this.offset
    )
}