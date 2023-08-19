package com.ramonguimaraes.horacerta.domain.resource

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val exception: java.lang.Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    fun <ToMap> mapResourceSuccess(getData: (R) -> ToMap): Resource<ToMap> {
        return when (this) {
            is Success -> {
                Success(getData(result))
            }
            is Loading -> {
                this
            }
            is Failure -> {
                this
            }
        }
    }
}
