package com.ramonguimaraes.horacerta.domain.companyProfile.useCase

import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository

class SaveCompanyProfileUseCase(private val repository: CompanyProfileRepository, private val userUseCase: GetCurrentUserUseCase) {
    suspend operator fun invoke(companyProfile: CompanyProfile) {
        companyProfile.companyUid = userUseCase.currentUid().toString()
        repository.save(companyProfile)
    }
}
