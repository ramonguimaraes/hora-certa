package com.ramonguimaraes.horacerta.domain.companiesList.useCase

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class LoadCompaniesUseCase(
    private val companyProfileRepository: CompanyProfileRepository
) {

    suspend fun execute(city: String, uf: String): Resource<List<CompanyProfile>> {
        return companyProfileRepository.load(city = city, uf = uf)
    }

    suspend fun execute(segment: String, city: String, uf: String): Resource<List<CompanyProfile>> {
        return companyProfileRepository.loadBySegment(segment, city, uf)
    }
}
