package com.ramonguimaraes.horacerta.domain.base

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: String) : Result<Nothing>()

    fun <MAP> mapSuccess(getData: (R) -> MAP): Result<MAP> {
        return when(this) {
            is Success -> Success(getData(data))
            is Loading -> this
            is Failure -> this
        }
    }
}