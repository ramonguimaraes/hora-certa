package com.ramonguimaraes.horacerta.di.companyProfile

import com.ramonguimaraes.horacerta.data.companyProfile.CompanyProfileRepositoryImpl
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.CompanyProfileValidationUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.SaveCompanyProfileUseCase
import com.ramonguimaraes.horacerta.presenter.companyProfile.CompanyProfileViewModel
import com.ramonguimaraes.horacerta.presenter.companyProfile.mapper.CompanyProfileViewMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun companyProfileModule() = module {
    factory<CompanyProfileRepository> { CompanyProfileRepositoryImpl(get(), get()) }
    factory { SaveCompanyProfileUseCase(get(), get()) }
    factory { LoadCompanyProfileUseCase(get(), get()) }
    factory { CompanyProfileViewMapper() }
    factory { CompanyProfileValidationUseCase() }
    viewModel { CompanyProfileViewModel(get(),get(), get(), get()) }
}