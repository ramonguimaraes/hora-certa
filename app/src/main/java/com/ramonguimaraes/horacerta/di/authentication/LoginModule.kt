package com.ramonguimaraes.horacerta.di.authentication

import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.authentication.useCase.LoginUseCase
import com.ramonguimaraes.horacerta.domain.authentication.useCase.LogoutUseCase
import com.ramonguimaraes.horacerta.presenter.authentication.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun loginModule() = module {
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}
