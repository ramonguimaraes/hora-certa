package com.ramonguimaraes.horacerta.domain.companiesList.repository

import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile

interface CompaniesRepository {
    suspend fun loadCompanies(): List<CompanyProfile>
}
