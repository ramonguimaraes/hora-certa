package com.ramonguimaraes.horacerta.utils

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

fun Calendar.toLocalTime(): LocalTime {
    return LocalTime.of(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), get(Calendar.SECOND))
}

fun Calendar.formattedDate(): String {
    val dayFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("PT-BR"))
    val dayOfWeek = dayFormat.format(time).split("-")[0].replaceFirstChar { it.uppercase() }

    val monthFormat = SimpleDateFormat("MMM", Locale.forLanguageTag("PT-BR"))
    val monthName =
        monthFormat.format(time).subSequence(0, 3).toString().replaceFirstChar { it.uppercase() }

    return "$dayOfWeek - ${get(Calendar.DAY_OF_MONTH)} $monthName. ${get(Calendar.YEAR)}"
}
