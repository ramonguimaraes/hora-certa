package com.ramonguimaraes.horacerta.di.service

import com.ramonguimaraes.horacerta.data.services.dataRepository.ServiceRepositoryImpl
import com.ramonguimaraes.horacerta.data.services.dataRepository.ServicesRepositoryImpl
import com.ramonguimaraes.horacerta.domain.services.repository.ServiceRepository
import com.ramonguimaraes.horacerta.domain.services.ServicesRepository
import com.ramonguimaraes.horacerta.domain.services.userCase.LoadServicesUseCase
import com.ramonguimaraes.horacerta.presenter.service.ServiceListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun serviceModule() = module {
    factory<ServicesRepository> { ServicesRepositoryImpl(get()) }
    factory<ServiceRepository> { ServiceRepositoryImpl(get()) }
    factory { LoadServicesUseCase(get()) }
    viewModel { ServiceListViewModel(get(), get()) }
}
