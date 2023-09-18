package com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository

class DeleteScheduleConfigUseCase(private val repository: ScheduleConfigRepository) {
    suspend operator fun invoke(scheduleConfig: ScheduleConfig): Resource<Boolean> {
        return repository.delete(scheduleConfig.id)
    }
}
