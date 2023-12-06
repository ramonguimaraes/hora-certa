package com.ramonguimaraes.horacerta.domain.schedule.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.Calendar

@Parcelize
data class ScheduledTime(
    var id: String= "",
    var time: Calendar = Calendar.getInstance(),
    val clientUid: String = "",
    val companyUid: String = ""
): Parcelable

fun ScheduledTime.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "id" to id,
        "time" to  time.toTimeStamp(),
        "clientUid" to  clientUid,
        "companyUid" to companyUid
    )
}

fun Calendar.toTimeStamp(): Timestamp {
    return Timestamp(time)
}