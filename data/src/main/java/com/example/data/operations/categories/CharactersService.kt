package com.example.data.operations.categories

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersService {
    @GET("v1/public/characters")
    fun getCharacters(
        @Query("ts") timestamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Long,
        @Query("limit") limit: Long = 200
    ): Deferred<Response<ResponseBody>>
}