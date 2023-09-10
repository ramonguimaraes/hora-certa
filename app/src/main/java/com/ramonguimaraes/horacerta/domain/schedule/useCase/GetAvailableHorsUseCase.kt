package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.utils.toLocalTime
import java.time.LocalTime
import java.util.Calendar

class GetAvailableHorsUseCase(private val repository: ScheduleRepository) {

    suspend fun getAvailableHors(calendar: Calendar): List<TimeInterval> {

        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val result = mutableListOf<ScheduledTime>()
        repository.load(calendar.time).mapResourceSuccess {
            result.addAll(it)
        }
        val lista = geraLista(
            LocalTime.of(8, 0),
            LocalTime.of(18, 0),
            calendar
        )
        result.forEach { scheduled ->
            val h = scheduled.time.get(Calendar.HOUR_OF_DAY)
            val m = scheduled.time.get(Calendar.MINUTE)
            val a = LocalTime.of(h, m)
            lista.forEach { interval ->
                if (interval.time == a) {
                    interval.disponivel = false
                    interval.show = false
                }
            }
        }

        return lista
    }

    private fun geraLista(
        inicio: LocalTime,
        fim: LocalTime,
        calendar: Calendar
    ): MutableList<TimeInterval> {
        val now = Calendar.getInstance()
        var horario = inicio
        val list = mutableListOf<TimeInterval>()

        if (now.time.before(calendar.time)) {
            horario = LocalTime.of(8, 0, 0)
        } else {
            while (horario.isBefore(now.toLocalTime())) {
                horario = horario.plusMinutes(INTERVAL)
            }
        }

        while (horario.isBefore(fim)) {
            list.add(TimeInterval(horario))
            horario = horario.plusMinutes(INTERVAL)
        }

        return list
    }

    private companion object {
        private const val INTERVAL = 30L
    }
}
