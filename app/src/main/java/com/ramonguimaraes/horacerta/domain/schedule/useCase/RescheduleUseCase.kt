package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import java.util.Calendar

class RescheduleUseCase(private val repository: ScheduleRepository) {

    suspend fun execute(appointmentId: String, scheduledTimes: List<ScheduledTime>, newDate: Calendar, createdBy: String): Resource<Boolean> {
        return repository.reschedule(appointmentId, scheduledTimes, newDate, createdBy)
    }
}
