package com.ramonguimaraes.horacerta.domain.companyProfile.repository

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource

interface CompanyProfileRepository {
    suspend fun save(companyProfile: CompanyProfile): Resource<Boolean>
    suspend fun update(companyProfile: CompanyProfile): Resource<Boolean>
    suspend fun load(uid: String?): Resource<CompanyProfile?>
}
