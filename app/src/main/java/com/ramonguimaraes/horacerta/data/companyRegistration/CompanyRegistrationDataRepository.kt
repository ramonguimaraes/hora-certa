package com.ramonguimaraes.horacerta.data.companyRegistration

import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import com.ramonguimaraes.horacerta.domain.companyRegistration.repository.CompanyRegisterRepository

class CompanyRegistrationDataRepository(
    private val companyRemoteDataSource: CompanyRemoteDataSource
) : CompanyRegisterRepository {
    override suspend fun saveCompany(company: Company) =
        companyRemoteDataSource.saveCompany(company)
}
