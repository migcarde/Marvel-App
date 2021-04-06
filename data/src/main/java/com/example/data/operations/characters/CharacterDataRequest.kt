package com.example.data.operations.characters

import com.example.commons.hash.toMD5Hash
import com.example.data.BuildConfig
import com.example.data.operations.base.BaseDataRequest
import com.example.domain.operations.characters.CharacterDataInput

data class CharacterDataRequest(
    override val timestamp: String,
    override val apiKey: String,
    override val hash: String,
    val id: Long
) : BaseDataRequest()

fun CharacterDataInput.toRequest(): CharacterDataRequest {
    val hash = this.timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY

    return CharacterDataRequest(
        timestamp = this.timestamp,
        apiKey = BuildConfig.PUBLIC_KEY,
        hash = hash.toMD5Hash(),
        id = this.id
    )
}