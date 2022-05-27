package com.ramonguimaraes.horacerta.domain.companyRegistration.repository

import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import kotlinx.coroutines.flow.Flow

interface CompanyRegisterRepository {
    suspend fun saveCompany(company: Company): Flow<Result<String>>
}
