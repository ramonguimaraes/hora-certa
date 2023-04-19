package com.ramonguimaraes.horacerta.di.authentication

import com.google.firebase.auth.FirebaseAuth
import com.ramonguimaraes.horacerta.data.authentication.dataRepository.AuthRepositoryImpl
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.authentication.useCase.LoginUseCase
import com.ramonguimaraes.horacerta.domain.authentication.useCase.SingUpUseCase
import com.ramonguimaraes.horacerta.presenter.authentication.CreateAccountViewModel
import com.ramonguimaraes.horacerta.presenter.authentication.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun authModule() = module {
    single { FirebaseAuth.getInstance() }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { SingUpUseCase(get()) }
    viewModel { CreateAccountViewModel(get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
