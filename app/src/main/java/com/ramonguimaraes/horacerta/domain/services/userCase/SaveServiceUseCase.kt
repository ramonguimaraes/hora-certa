package com.ramonguimaraes.horacerta.domain.services.userCase

import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.repository.ServiceRepository

class SaveServiceUseCase(private val repository: ServiceRepository, private val userUseCase: GetCurrentUserUseCase) {

    suspend operator fun invoke(service: Service) {
        service.companyUid = userUseCase.currentUid().toString()
        if (service.id.isNotBlank()) {
            repository.update(service)
        } else {
            repository.save(service)
        }
    }
}
