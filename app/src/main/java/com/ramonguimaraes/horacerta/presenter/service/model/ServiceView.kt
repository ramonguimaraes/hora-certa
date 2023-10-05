package com.ramonguimaraes.horacerta.presenter.service.model

data class ServiceView(
    var id: String = "",
    var title: String = "",
    var titleError: String? = null,
    var price: Double = 0.0,
    var priceError: String? = null,
    var duration: Long = 0L,
    var durationError: String? = null,
)
