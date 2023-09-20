package com.ramonguimaraes.horacerta.domain.services.userCase

import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.repository.ServiceRepository

class DeleteServiceUseCase(private val repository: ServiceRepository) {
    suspend operator fun invoke(service: Service) {
        repository.delete(service)
    }
}
