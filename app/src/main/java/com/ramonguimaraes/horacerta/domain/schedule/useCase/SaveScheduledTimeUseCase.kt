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
            date = calendar,
        )

        return repository.save(appointment, scheduledTimes)
    }

    /*suspend fun save(
        calendar: Calendar,
        timeNeeded: Int,
        companyUid: String,
        clientUid: String = ""
    ): Resource<Boolean>? {
        val intervals = timeNeeded / 30
        val scheduledTimes = mutableListOf<ScheduledTime>()
        for (i in 0 until intervals) {
            scheduledTimes.add(
                ScheduledTime(
                    (calendar.clone() as Calendar),
                    clientUid,
                    companyUid
                )
            )
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30)
        }

        val appointment = Appointment(
            time = calcularIntervaloDeTempo(scheduledTimes),
            client = clientUid,
            service = "services",
            companyUid = companyUid,
            calendar
        )

        return repository.save(appointment, scheduledTimes)
    }*/

    private fun calcularIntervaloDeTempo(lista: List<ScheduledTime>): String {
        if (lista.isEmpty()) return ""

        val horaMaisRecente = lista.maxByOrNull { it.time.timeInMillis }
        val horaMaisAntiga = lista.minByOrNull { it.time.timeInMillis }

        val formato = SimpleDateFormat("HH:mm")

        val horaRecenteStr = formato.format(horaMaisRecente?.time?.time)
        val horaAntigaStr = formato.format(horaMaisAntiga?.time?.time)

        return "$horaAntigaStr - $horaRecenteStr"
    }
}
