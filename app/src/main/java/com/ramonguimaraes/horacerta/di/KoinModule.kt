package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.di.authentication.authModule
import com.ramonguimaraes.horacerta.di.authentication.createAccountModule
import com.ramonguimaraes.horacerta.di.authentication.firebaseModule
import com.ramonguimaraes.horacerta.di.authentication.loginModule
import com.ramonguimaraes.horacerta.di.companies.companiesModule
import com.ramonguimaraes.horacerta.di.companyProfile.companyProfileModule
import com.ramonguimaraes.horacerta.di.home.homeModule
import com.ramonguimaraes.horacerta.di.schedule.scheduleModule
import com.ramonguimaraes.horacerta.di.scheduleConfig.scheduleConfigModule
import com.ramonguimaraes.horacerta.di.service.serviceModule
import com.ramonguimaraes.horacerta.di.splashScreen.splashScreenModule

object KoinModule {
    fun modules() = listOf(
        firebaseModule(),
        authModule(),
        createAccountModule(),
        loginModule(),
        scheduleModule(),
        serviceModule(),
        scheduleConfigModule(),
        companyProfileModule(),
        homeModule(),
        splashScreenModule(),
        companiesModule()
    )
}
