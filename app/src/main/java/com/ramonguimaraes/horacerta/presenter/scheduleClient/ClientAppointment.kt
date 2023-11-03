package com.ramonguimaraes.horacerta.presenter.scheduleClient

import android.net.Uri
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.services.model.Service
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

data class ClientAppointment(
    val appointmentId: String,
    val scheduledTimes: List<ScheduledTime>,
    val services: List<Service>,
    val companyUid: String,
    val companyName: String,
    var photoUri: Uri,
    var phone: String,
    var date: Calendar,
    var showDateLabel: Boolean = false,
    var latitude: Double?,
    var longitude: Double?
): Serializable {

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

    private fun dateFormat(calendar: Calendar): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(calendar.time)
    }
}