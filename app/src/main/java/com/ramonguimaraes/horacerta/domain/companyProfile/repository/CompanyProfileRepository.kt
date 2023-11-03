package com.ramonguimaraes.horacerta.domain.companyProfile.repository

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource

interface CompanyProfileRepository {
    suspend fun save(companyProfile: CompanyProfile): Resource<Boolean>
    suspend fun update(companyProfile: CompanyProfile): Resource<Boolean>
    suspend fun load(uid: String?): Resource<CompanyProfile?>
    suspend fun load(city: String, uf: String): Resource<List<CompanyProfile>>
    suspend fun loadBySegment(segment: String, city: String, uf: String): Resource<List<CompanyProfile>>
}
