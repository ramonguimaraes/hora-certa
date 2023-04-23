package com.ramonguimaraes.horacerta.di.authentication

import com.ramonguimaraes.horacerta.domain.authentication.useCase.LoginUseCase
import com.ramonguimaraes.horacerta.presenter.authentication.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun loginModule() = module {
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
