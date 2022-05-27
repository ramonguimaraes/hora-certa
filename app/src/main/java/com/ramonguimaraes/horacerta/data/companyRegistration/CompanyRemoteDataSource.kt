package com.ramonguimaraes.horacerta.data.companyRegistration

import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import kotlinx.coroutines.flow.Flow

interface CompanyRemoteDataSource {
    suspend fun saveCompany(company: Company): Flow<Result<String>>
}
