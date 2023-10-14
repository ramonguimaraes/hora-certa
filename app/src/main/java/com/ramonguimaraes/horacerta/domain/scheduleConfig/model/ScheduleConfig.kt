package com.ramonguimaraes.horacerta.domain.scheduleConfig.model

import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek
import java.time.LocalTime

data class ScheduleConfig(
    val id: String = "",
    val companyUid: String = "",
    var dayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    var openTime: LocalTime = LocalTime.of(7, 0, 0),
    var intervalStart: LocalTime = LocalTime.of(12, 0, 0),
    var intervalEnd: LocalTime = LocalTime.of(13, 0, 0),
    var closeTime: LocalTime = LocalTime.of(18, 0, 0)
)

fun ScheduleConfig.toHashMap(): Map<String, Any> {
    return mapOf(
        "companyUid" to companyUid,
        "dayOfWeek" to dayOfWeek,
        "openTime" to openTime.toString(),
        "intervalStart" to intervalStart.toString(),
        "intervalEnd" to intervalEnd.toString(),
        "closeTime" to closeTime.toString()
    )
}