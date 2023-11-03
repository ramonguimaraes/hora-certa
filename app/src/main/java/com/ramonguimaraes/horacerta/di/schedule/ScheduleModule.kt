package com.ramonguimaraes.horacerta.di.schedule

import com.ramonguimaraes.horacerta.data.schedule.dataRepository.ScheduleRepositoryImpl
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.useCase.DeleteScheduledTimeUseCase
import com.ramonguimaraes.horacerta.domain.schedule.useCase.GetAvailableHorsUseCase
import com.ramonguimaraes.horacerta.domain.schedule.useCase.SaveScheduledTimeUseCase
import com.ramonguimaraes.horacerta.presenter.schedule.viewModel.ScheduleRegistrationViewModel
import com.ramonguimaraes.horacerta.presenter.schedule.viewModel.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun scheduleModule() = module {
    factory<ScheduleRepository> { ScheduleRepositoryImpl(get()) }
    factory { GetAvailableHorsUseCase(get(), get()) }
    factory { SaveScheduledTimeUseCase(get()) }
    factory { DeleteScheduledTimeUseCase(get()) }
    viewModel { ScheduleRegistrationViewModel(get(), get(), get(), get(), get()) }
    viewModel { ScheduleViewModel(get(), get(), get()) }
}
