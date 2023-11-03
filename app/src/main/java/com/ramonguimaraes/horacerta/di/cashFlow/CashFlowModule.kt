package com.ramonguimaraes.horacerta.di.cashFlow

import com.ramonguimaraes.horacerta.data.cashFlow.CashFlowRepositoryImpl
import com.ramonguimaraes.horacerta.domain.cashFlow.repository.CashFlowRepository
import com.ramonguimaraes.horacerta.presenter.cashFlow.viewModel.CashFlowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun cashFlowModule() = module {
    factory<CashFlowRepository> { CashFlowRepositoryImpl(get()) }
    viewModel { CashFlowViewModel(get(), get()) }
}
