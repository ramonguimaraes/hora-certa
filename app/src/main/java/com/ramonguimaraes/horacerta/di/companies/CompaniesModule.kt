package com.ramonguimaraes.horacerta.di.companies

import com.ramonguimaraes.horacerta.domain.companiesList.useCase.LoadCompaniesUseCase
import com.ramonguimaraes.horacerta.presenter.companiesList.viewModel.CompaniesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun companiesModule() = module {
    factory { LoadCompaniesUseCase(get()) }
    viewModel { CompaniesViewModel(get()) }
}