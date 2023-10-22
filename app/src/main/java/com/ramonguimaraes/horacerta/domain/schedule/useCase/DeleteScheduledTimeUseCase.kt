package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ClientAppointment

class DeleteScheduledTimeUseCase(
    private val scheduleRepository: ScheduleRepository
) {

    suspend fun delete(appointment: ClientAppointment): Resource<Boolean> {
        return scheduleRepository.delete(appointment)
    }
}