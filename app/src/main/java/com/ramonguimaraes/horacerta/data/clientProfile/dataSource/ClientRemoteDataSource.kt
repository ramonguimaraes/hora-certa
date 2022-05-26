package com.ramonguimaraes.horacerta.data.clientProfile.dataSource

import com.ramonguimaraes.horacerta.data.clientProfile.model.ClientData

interface ClientRemoteDataSource {
    suspend fun getClient(): ClientData
    suspend fun saveClient(client: ClientData): Boolean
}
