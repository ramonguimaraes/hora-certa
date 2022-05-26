package com.ramonguimaraes.horacerta.presentation.clientProfile.mapper

import com.ramonguimaraes.horacerta.domain.clientProfile.model.ClientModel
import com.ramonguimaraes.horacerta.presentation.base.ViewMapper
import com.ramonguimaraes.horacerta.presentation.clientProfile.model.ClientView

class ClientViewMapper: ViewMapper<ClientView, ClientModel> {
    override fun toView(domain: ClientModel): ClientView {
        return ClientView(
            domain.uid,
            domain.name,
            domain.phone,
            domain.email
        )
    }

    override fun fromView(view: ClientView): ClientModel {
        return ClientModel(
            view.uid,
            view.name,
            view.phone,
            view.email
        )
    }
}
