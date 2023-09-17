package com.ramonguimaraes.horacerta.di.scheduleConfig

import com.ramonguimaraes.horacerta.data.scheduleConfig.ScheduleConfigRepositoryImpl
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository
import com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase.SaveScheduleConfigUseCase
import com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase.ScheduleConfigListUseCase
import com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel.ScheduleConfigListViewModel
import com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel.ScheduleConfigViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun scheduleConfigModule() = module {
    factory<ScheduleConfigRepository> { ScheduleConfigRepositoryImpl(get()) }
    factory { SaveScheduleConfigUseCase(get()) }
    factory { ScheduleConfigListUseCase(get(), get()) }
    viewModel { ScheduleConfigViewModel(get(), get()) }
    viewModel { ScheduleConfigListViewModel(get())}
}
