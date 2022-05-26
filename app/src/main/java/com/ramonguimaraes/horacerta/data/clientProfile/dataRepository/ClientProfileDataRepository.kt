package com.ramonguimaraes.horacerta.data.clientProfile.dataRepository

import com.ramonguimaraes.horacerta.data.clientProfile.dataSource.ClientProfileDataFactory
import com.ramonguimaraes.horacerta.data.clientProfile.mapper.ClientDataMapper
import com.ramonguimaraes.horacerta.domain.clientProfile.model.ClientModel
import com.ramonguimaraes.horacerta.domain.clientProfile.repository.ClientProfileRepository

class ClientProfileDataRepository(
    private val dataFactory: ClientProfileDataFactory,
    private val mapper: ClientDataMapper
) :
    ClientProfileRepository {
    override suspend fun getClient(): ClientModel {
        val result = dataFactory.getRemote().getClient()
        return mapper.fromEntity(result)
    }

    override suspend fun saveClient(clientModel: ClientModel): Boolean {
        val mappedClient = mapper.toEntity(clientModel)
        return dataFactory.getRemote().saveClient(mappedClient)
    }
}
