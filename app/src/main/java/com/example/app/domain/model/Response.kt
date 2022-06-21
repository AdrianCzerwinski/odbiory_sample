package com.example.app.domain.model

sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Error(
        val message: String
    ): Response<Nothing>()

    data class ErrorMessageBar<out T>(
        val data: T
    ): Response<T>()
}
