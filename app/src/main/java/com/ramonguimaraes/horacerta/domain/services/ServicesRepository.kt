package com.ramonguimaraes.horacerta.domain.services

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ServiceItem

interface ServicesRepository {
    suspend fun load(companyUid: String): Resource<List<ServiceItem>>
}
