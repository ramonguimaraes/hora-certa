package com.ramonguimaraes.horacerta.domain.services.userCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.repository.ServiceRepository

class LoadServicesUseCase(private val repository: ServiceRepository) {

    suspend operator fun invoke(companyUid: String): Resource<List<Service>> {
        return repository.loadAll(companyUid)
    }
}