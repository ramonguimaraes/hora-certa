package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.user.model.User
import java.text.SimpleDateFormat
import java.util.Calendar

class SaveScheduledTimeUseCase(private val repository: ScheduleRepository) {

    suspend fun save(
        calendar: Calendar,
        timeNeeded: Int,
        companyUid: String,
        services: List<Service>,
        user: User
    ): Resource<Boolean> {
        val intervals = timeNeeded / 30
        val scheduledTimes = mutableListOf<ScheduledTime>()
        val calendarAux = (calendar.clone() as Calendar)
        for (i in 0 until intervals) {
            scheduledTimes.add(
                ScheduledTime(
                    time = (calendar.clone() as Calendar),
                    clientUid = user.uid,
                    companyUid = companyUid
                )
            )
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30)
        }

        val appointment = Appointment(
            id = "",
            scheduledTimes = scheduledTimes,
            services = services,
            companyUid = companyUid,
            clientUid = user.uid,
            clientName = user.name,
            date = calendarAux,
        )

        return repository.save(appointment, scheduledTimes, user.uid)
    }
}
