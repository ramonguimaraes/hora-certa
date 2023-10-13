package com.ramonguimaraes.horacerta.domain.schedule.model

import com.ramonguimaraes.horacerta.domain.services.model.Service
import java.text.SimpleDateFormat
import java.util.Calendar

data class Appointment(
    val scheduledTimes: List<ScheduledTime>,
    val services: List<Service>,
    val companyUid: String,
    val clientUid: String,
    val clientName: String,
    val date: Calendar
) {
    fun getHourString(): String {
        val sortedList = scheduledTimes.sortedBy { it.time }
        if (sortedList.isNotEmpty()) {
            val first = sortedList.first().time
            val last = sortedList.last().time
            val a = dateFormat(first)
            last.add(Calendar.MINUTE, 30)
            val b = dateFormat(last)
            return "$a - $b"
        }
        return ""
    }

    fun getServices(): String {
        if (services.isNotEmpty()) {
            val servicesTitle = services.map { it.title }
            var list = ""
            servicesTitle.forEach {
                list += "$it\n"
            }
            return list
        }
        return ""
    }

    private fun dateFormat(calendar: Calendar): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(calendar.time)
    }
}
