package com.example.commons.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonParserImpl : JsonParser {

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    override fun <T> fromJson(json: String, type: Class<T>): T = moshi.adapter(type).lenient().fromJson(json)!!

    override fun <T> fromJsonList(json: String, type: Class<T>): List<T>  {
        val listType = Types.newParameterizedType(List::class.java, type)

        return moshi.adapter<List<T>>(listType).lenient().fromJson(json)!!
    }

    override fun <T : Any> toJson(value: T): String = moshi.adapter<T>(value::class.java).toJson(value)
}