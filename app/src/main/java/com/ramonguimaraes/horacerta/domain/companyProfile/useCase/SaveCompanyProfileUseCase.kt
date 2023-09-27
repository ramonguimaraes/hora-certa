package com.ramonguimaraes.horacerta.domain.companyProfile.useCase

import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class SaveCompanyProfileUseCase(
    private val repository: CompanyProfileRepository,
    private val userUseCase: GetCurrentUserUseCase
) {
    suspend operator fun invoke(companyProfile: CompanyProfile): Resource<Boolean> {
        companyProfile.companyUid = userUseCase.currentUid().toString()
        return if (companyProfile.id.isNotBlank()) {
            repository.update(companyProfile)
        } else {
            repository.save(companyProfile)
        }
    }
}
