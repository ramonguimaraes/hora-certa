package com.ramonguimaraes.horacerta.domain.companyProfile.repository

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource

interface CompanyProfileRepository {
    suspend fun save(companyProfile: CompanyProfile)
    suspend fun update(companyProfile: CompanyProfile)
    suspend fun load(uid: String?): Resource<CompanyProfile?>
}
