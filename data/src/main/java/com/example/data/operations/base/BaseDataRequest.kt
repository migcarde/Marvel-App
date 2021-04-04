package com.example.data.operations.base

abstract class BaseDataRequest {
    abstract val timestamp: String
    abstract val apiKey: String
    abstract val hash: String
}