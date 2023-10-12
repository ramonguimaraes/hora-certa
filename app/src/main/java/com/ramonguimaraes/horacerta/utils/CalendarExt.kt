package com.ramonguimaraes.horacerta.utils

import java.util.Calendar

fun Calendar.onlyDate(): Calendar {
    val calendar = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}
