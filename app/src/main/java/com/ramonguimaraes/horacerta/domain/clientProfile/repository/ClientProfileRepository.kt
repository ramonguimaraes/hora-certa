package com.ramonguimaraes.horacerta.domain.clientProfile.repository

import com.ramonguimaraes.horacerta.domain.clientProfile.model.ClientModel

interface ClientProfileRepository {
    suspend fun getClient(): ClientModel?
    suspend fun saveClient(clientModel: ClientModel): Boolean
}
