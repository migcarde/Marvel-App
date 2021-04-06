package com.example.data.operations.characters

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {
    @GET("v1/public/characters")
    fun getCharacters(
        @Query("ts") timestamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Long
    ): Deferred<Response<ResponseBody>>

    @GET("v1/public/characters/{id}")
    fun getCharacter(
        @Path("id") id: Long,
        @Query("ts") timestamp: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
    ): Deferred<Response<ResponseBody>>
}