package com.ramonguimaraes.horacerta.domain.services.repository

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.services.model.Service

interface ServiceRepository {
    suspend fun load(id: String): Resource<Service>
    suspend fun loadAll(companyUid: String): Resource<List<Service>>
    suspend fun save(service: Service)
    suspend fun update(service: Service)
    suspend fun delete(service: Service)
}
