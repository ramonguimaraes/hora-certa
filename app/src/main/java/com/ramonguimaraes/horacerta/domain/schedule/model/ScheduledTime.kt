package com.ramonguimaraes.horacerta.domain.schedule.model

import com.google.firebase.Timestamp
import java.util.Calendar

data class ScheduledTime(
    val uid: String = "",
    val time: Calendar = Calendar.getInstance(),
    val clientUid: String = "",
    val companyUid: String = ""
)

fun ScheduledTime.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "uid" to  uid,
        "time" to  time.toTimeStamp(),
        "clientUid" to  clientUid,
        "companyUid" to companyUid
    )
}


fun Calendar.toTimeStamp(): Timestamp {
    return Timestamp(time)
}