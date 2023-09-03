package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.di.authentication.*
import com.ramonguimaraes.horacerta.di.schedule.getScheduleModule

object KoinModule {
    fun modules() = listOf(
        firebaseModule(),
        authModule(),
        createAccountModule(),
        loginModule(),
        getScheduleModule()
    )
}
