package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.di.authentication.authModule

object KoinModule {
    fun modules() = listOf(
        authModule()
    )
}
