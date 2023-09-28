package com.ramonguimaraes.horacerta.domain.services.model

class Service(
    var id: String = "",
    var companyUid: String = "",
    var title: String = "",
    var price: Double = 0.0,
    var estimatedDuration: Long = 0,
    var checked: Boolean = false
)

fun Service.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "companyUid" to companyUid,
        "title" to title,
        "price" to price,
        "estimatedDuration" to estimatedDuration
    )
}
