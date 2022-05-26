package com.ramonguimaraes.horacerta.remote.clientProfile.mapper

import com.ramonguimaraes.horacerta.data.clientProfile.model.ClientData
import com.ramonguimaraes.horacerta.remote.base.RemoteMapper
import com.ramonguimaraes.horacerta.remote.clientProfile.model.ClientRemote

class ClientRemoteMapper: RemoteMapper<ClientRemote, ClientData> {
    override fun fromRemote(remote: ClientRemote): ClientData {
        return ClientData(
            remote.uid,
            remote.name,
            remote.phone,
            remote.email
        )
    }

    override fun toRemote(entity: ClientData): ClientRemote {
        return ClientRemote(
            entity.uid,
            entity.name,
            entity.phone,
            entity.email
        )
    }
}
