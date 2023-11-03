package com.ramonguimaraes.horacerta.domain.services.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Service(
    //var uid: UUID = UUID.randomUUID(),
    var id: String = "",
    var companyUid: String = "",
    var title: String = "",
    var price: Double = 0.0,
    var estimatedDuration: Long = 0,
    var checked: Boolean = false
): Parcelable

fun Service.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        //"uid" to uid.toString(),
        "id" to id,
        "companyUid" to companyUid,
        "title" to title,
        "price" to price,
        "estimatedDuration" to estimatedDuration
    )
}
