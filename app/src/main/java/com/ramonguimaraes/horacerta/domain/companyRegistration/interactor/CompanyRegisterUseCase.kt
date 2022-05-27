package com.ramonguimaraes.horacerta.domain.companyRegistration.interactor

import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import com.ramonguimaraes.horacerta.domain.companyRegistration.repository.CompanyRegisterRepository

class CompanyRegisterUseCase(private val repository: CompanyRegisterRepository) {
    suspend fun invoke(company: Company) = repository.saveCompany(company)
}
