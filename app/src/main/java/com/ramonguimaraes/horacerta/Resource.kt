package com.ramonguimaraes.horacerta

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val exception: java.lang.Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
