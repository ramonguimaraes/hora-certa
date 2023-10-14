package com.ramonguimaraes.horacerta.di.splashScreen

import com.ramonguimaraes.horacerta.ui.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getSplashScreenModule() = module {
    viewModel { SplashScreenViewModel(get(), get(), get()) }
}
