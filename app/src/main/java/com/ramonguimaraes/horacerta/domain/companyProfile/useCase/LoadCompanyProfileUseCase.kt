package com.ramonguimaraes.horacerta.domain.companyProfile.useCase

import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class LoadCompanyProfileUseCase(private val repository: CompanyProfileRepository, private val userUseCase: GetCurrentUserUseCase) {
    suspend operator fun invoke(): Resource<CompanyProfile?> {
        val uid = userUseCase.currentUid()
        return repository.load(uid)
    }
}
