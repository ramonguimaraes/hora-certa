package com.ramonguimaraes.horacerta.presenter.service.viewMapper

import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.presenter.service.model.ServiceView
import com.ramonguimaraes.horacerta.presenter.viewUtils.viewMapper.ViewMapper

class ServiceViewMapper : ViewMapper<ServiceView, Service>() {
    override fun mapToView(model: Service): ServiceView {
        return ServiceView(
            id = model.id,
            title = model.title,
            price = model.price,
            duration = model.estimatedDuration
        )
    }

    override fun mapFromView(view: ServiceView): Service {
        return Service(
            id = view.id,
            title = view.title,
            price = view.price,
            estimatedDuration = view.duration
        )
    }
}
