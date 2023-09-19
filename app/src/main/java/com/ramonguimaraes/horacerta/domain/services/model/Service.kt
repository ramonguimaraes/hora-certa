package com.ramonguimaraes.horacerta.domain.services.model

class Service(
    val id: String,
    val companyUid: String,
    val title: String,
    val price: Double,
    val estimatedDuration: Long
)

fun Service.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "id" to id,
        "companyUid" to companyUid,
        "title" to title,
        "price" to price,
        "estimatedDuration" to estimatedDuration
    )
}
