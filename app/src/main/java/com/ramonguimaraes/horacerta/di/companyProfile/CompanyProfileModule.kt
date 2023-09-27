package com.ramonguimaraes.horacerta.di.companyProfile

import com.ramonguimaraes.horacerta.data.companyProfile.CompanyProfileRepositoryImpl
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.SaveCompanyProfileUseCase
import com.ramonguimaraes.horacerta.presenter.companyProfile.CompanyProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun companyProfileModule() = module {
    factory<CompanyProfileRepository> { CompanyProfileRepositoryImpl(get(), get()) }
    factory { SaveCompanyProfileUseCase(get(), get()) }
    factory { LoadCompanyProfileUseCase(get(), get()) }
    viewModel { CompanyProfileViewModel(get(),get()) }
}