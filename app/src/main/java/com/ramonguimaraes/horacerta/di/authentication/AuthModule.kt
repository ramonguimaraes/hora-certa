package com.ramonguimaraes.horacerta.di.authentication

import com.ramonguimaraes.horacerta.data.authentication.dataRepository.AuthRepositoryImpl
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import org.koin.dsl.module

fun authModule() = module {
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
}
