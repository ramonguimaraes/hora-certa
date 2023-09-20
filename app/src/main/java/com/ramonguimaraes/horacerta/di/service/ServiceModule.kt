package com.ramonguimaraes.horacerta.di.service

import com.ramonguimaraes.horacerta.data.services.dataRepository.ServiceRepositoryImpl
import com.ramonguimaraes.horacerta.data.services.dataRepository.ServicesRepositoryImpl
import com.ramonguimaraes.horacerta.domain.services.repository.ServiceRepository
import com.ramonguimaraes.horacerta.domain.services.ServicesRepository
import com.ramonguimaraes.horacerta.domain.services.userCase.DeleteServiceUseCase
import com.ramonguimaraes.horacerta.domain.services.userCase.LoadServicesUseCase
import com.ramonguimaraes.horacerta.domain.services.userCase.SaveServiceUseCase
import com.ramonguimaraes.horacerta.presenter.service.ServiceListViewModel
import com.ramonguimaraes.horacerta.presenter.service.ServiceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun serviceModule() = module {
    factory<ServicesRepository> { ServicesRepositoryImpl(get()) }
    factory<ServiceRepository> { ServiceRepositoryImpl(get()) }
    factory { LoadServicesUseCase(get()) }
    factory { SaveServiceUseCase(get(), get()) }
    viewModel { ServiceListViewModel(get(), get()) }
    viewModel { ServiceViewModel(get()) }
}
