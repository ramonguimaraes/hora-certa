package com.ramonguimaraes.horacerta.di.home

import com.ramonguimaraes.horacerta.presenter.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun homeModule() = module {
    viewModel { HomeViewModel(get(), get()) }
}