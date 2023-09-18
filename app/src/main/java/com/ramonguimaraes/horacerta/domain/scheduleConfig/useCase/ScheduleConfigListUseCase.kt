package com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase

import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository

class ScheduleConfigListUseCase(private val scheduleConfigRepository: ScheduleConfigRepository, private val currentUserUseCase: GetCurrentUserUseCase) {

    suspend operator fun invoke(): Resource<List<ScheduleConfig>> {
        val userUid = currentUserUseCase.currentUid()
        return scheduleConfigRepository.list(userUid)
    }
}
