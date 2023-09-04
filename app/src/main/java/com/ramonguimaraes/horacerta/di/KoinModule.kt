package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.di.authentication.*
import com.ramonguimaraes.horacerta.di.schedule.scheduleModule
import com.ramonguimaraes.horacerta.di.service.serviceModule

object KoinModule {
    fun modules() = listOf(
        firebaseModule(),
        authModule(),
        createAccountModule(),
        loginModule(),
        scheduleModule(),
        serviceModule()
    )
}
