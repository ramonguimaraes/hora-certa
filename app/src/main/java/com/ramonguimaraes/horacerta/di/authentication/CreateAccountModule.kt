package com.ramonguimaraes.horacerta.di.authentication

import com.ramonguimaraes.horacerta.data.profile.UserRepositoryImpl
import com.ramonguimaraes.horacerta.domain.authentication.useCase.SingUpUseCase
import com.ramonguimaraes.horacerta.domain.user.repository.UserRepository
import com.ramonguimaraes.horacerta.domain.user.useCase.SaveUserUseCase
import com.ramonguimaraes.horacerta.presenter.authentication.viewModel.CreateAccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createAccountModule() = module {
    factory<UserRepository> { UserRepositoryImpl(get()) }
    factory { SingUpUseCase(get()) }
    factory { SaveUserUseCase(get()) }
    viewModel { CreateAccountViewModel(get(), get()) }
}
