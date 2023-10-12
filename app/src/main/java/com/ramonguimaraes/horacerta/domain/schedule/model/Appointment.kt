package com.ramonguimaraes.horacerta.domain.schedule.model

import com.ramonguimaraes.horacerta.utils.onlyDate
import java.util.Calendar

data class Appointment(
    val time: String = "",
    val client: String = "",
    val service: String = "",
    val companyUid: String = "",
    val date: Calendar
)

fun Appointment.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "time" to time,
        "client" to client,
        "service" to service,
        "companyUid" to companyUid,
        "date" to date.onlyDate().toTimeStamp()
    )
}
