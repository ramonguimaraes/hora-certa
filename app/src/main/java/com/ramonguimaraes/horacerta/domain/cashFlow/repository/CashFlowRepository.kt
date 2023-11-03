package com.ramonguimaraes.horacerta.domain.cashFlow.repository

import com.ramonguimaraes.horacerta.domain.cashFlow.CashRegister
import com.ramonguimaraes.horacerta.domain.resource.Resource
import java.util.Date

interface CashFlowRepository {
    suspend fun save(cashRegister: CashRegister): Resource<CashRegister>
    suspend fun load(companyUid: String): Resource<List<CashRegister>>
    suspend fun load(companyUid: String, dateRange: Pair<Date, Date>): Resource<List<CashRegister>>
}
