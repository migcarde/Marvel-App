package com.example.domain

sealed class RepositoryFailure {

    object RepositoryException : RepositoryFailure()

    object NoInternet: RepositoryFailure()

    object Unauthorized : RepositoryFailure()

    object ServerError : RepositoryFailure()

    object Unknown : RepositoryFailure()
}