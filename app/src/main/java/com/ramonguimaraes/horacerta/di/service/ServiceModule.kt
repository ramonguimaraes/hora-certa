package com.ramonguimaraes.horacerta.di.service

import com.ramonguimaraes.horacerta.data.services.dataRepository.ServicesRepositoryImpl
import com.ramonguimaraes.horacerta.domain.services.ServicesRepository
import org.koin.dsl.module

fun serviceModule() = module {
    factory<ServicesRepository> { ServicesRepositoryImpl(get()) }
}
