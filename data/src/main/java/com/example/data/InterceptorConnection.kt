package com.example.data

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class InterceptorConnection : Interceptor {

    companion object {
        private const val CONTENT_TYPE = "Content-Type"
        private const val APPLICATION_JSON = "application/json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest: Request =
            request.newBuilder().addHeader(CONTENT_TYPE, APPLICATION_JSON).build()

        return chain.proceed(newRequest)
    }
}