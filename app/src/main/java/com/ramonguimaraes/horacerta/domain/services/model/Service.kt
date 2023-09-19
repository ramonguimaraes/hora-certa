package com.ramonguimaraes.horacerta.domain.services.model

class Service(
    var id: String = "",
    val companyUid: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val estimatedDuration: Long = 0
)

fun Service.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "companyUid" to companyUid,
        "title" to title,
        "price" to price,
        "estimatedDuration" to estimatedDuration
    )
}
