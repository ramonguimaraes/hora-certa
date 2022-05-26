package com.ramonguimaraes.horacerta.data.clientProfile.mapper

import com.ramonguimaraes.horacerta.data.base.DataMapper
import com.ramonguimaraes.horacerta.data.clientProfile.model.ClientData
import com.ramonguimaraes.horacerta.domain.clientProfile.model.ClientModel

class ClientDataMapper: DataMapper<ClientData, ClientModel> {
    override fun fromEntity(entity: ClientData): ClientModel {
        return ClientModel(
            entity.uid,
            entity.name,
            entity.phone,
            entity.email
        )
    }

    override fun toEntity(data: ClientModel): ClientData {
        return ClientData(
            data.uid,
            data.name,
            data.phone,
            data.email
        )
    }
}
