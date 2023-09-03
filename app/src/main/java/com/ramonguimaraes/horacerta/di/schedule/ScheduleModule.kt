package com.ramonguimaraes.horacerta.di.schedule

import com.ramonguimaraes.horacerta.data.schedule.dataRepository.ScheduleRepositoryImpl
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.useCase.GetAvailableHorsUseCase
import com.ramonguimaraes.horacerta.presenter.schedule.ScheduleRegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun getScheduleModule() = module {
    factory<ScheduleRepository> { ScheduleRepositoryImpl(get()) }
    factory { GetAvailableHorsUseCase(get()) }
    viewModel { ScheduleRegistrationViewModel(get(), get()) }
}
