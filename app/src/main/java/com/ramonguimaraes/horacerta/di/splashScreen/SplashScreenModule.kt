package com.ramonguimaraes.horacerta.di.splashScreen

import com.ramonguimaraes.horacerta.presenter.splashScreen.viewModel.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun splashScreenModule() = module {
    viewModel { SplashScreenViewModel(get(), get(), get()) }
}
