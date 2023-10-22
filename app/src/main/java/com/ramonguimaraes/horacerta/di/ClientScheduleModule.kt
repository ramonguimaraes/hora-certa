package com.ramonguimaraes.horacerta.di

import com.ramonguimaraes.horacerta.presenter.scheduleClient.viewModel.ClientScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun clientScheduleModule() = module {
    viewModel { ClientScheduleViewModel(get(), get()) }
}