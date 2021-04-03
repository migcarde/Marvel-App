package com.example.data

data class BaseResponse(
    val state: State
)

data class State(
    val status: String,
    val detail: String?
)