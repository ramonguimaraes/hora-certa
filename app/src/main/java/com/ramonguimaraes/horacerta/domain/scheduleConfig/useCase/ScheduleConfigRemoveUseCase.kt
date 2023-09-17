package com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository

class ScheduleConfigRemoveUseCase(private val scheduleConfigRepository: ScheduleConfigRepository) {

    suspend operator fun invoke(id: String): Resource<Boolean> {
        return scheduleConfigRepository.delete(id)
    }
}
