package com.ramonguimaraes.horacerta.domain.scheduleConfig.model

import com.ramonguimaraes.horacerta.utils.DayOfWeek
import java.time.LocalTime

data class ScheduleConfig(
    val companyUid: String = "",
    val dayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    val openTime: LocalTime = LocalTime.of(7, 0, 0),
    val intervalStart: LocalTime = LocalTime.of(12, 0, 0),
    val intervalEnd: LocalTime = LocalTime.of(13, 0, 0),
    val closeTime: LocalTime = LocalTime.of(18, 0, 0)
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