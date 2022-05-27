package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.data.companyRegistration.CompanyRegistrationDataRepository
import com.ramonguimaraes.horacerta.data.companyRegistration.CompanyRemoteDataSource
import com.ramonguimaraes.horacerta.domain.companyRegistration.interactor.CompanyRegisterUseCase
import com.ramonguimaraes.horacerta.domain.companyRegistration.repository.CompanyRegisterRepository
import com.ramonguimaraes.horacerta.presentation.companyRegistration.viewmodel.CompanyRegistrationViewModel
import com.ramonguimaraes.horacerta.remote.companyRegistration.CompanyService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun companyModule() = module {

    factory<CompanyRemoteDataSource> { CompanyService(get(), get()) }
    factory<CompanyRegisterRepository> { CompanyRegistrationDataRepository(get()) }
    factory { CompanyRegisterUseCase(get()) }
    single { CompanyRegistrationViewModel(get()) }
}
