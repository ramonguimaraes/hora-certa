package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository
import com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase.ScheduleConfigListUseCase
import com.ramonguimaraes.horacerta.domain.services.userCase.LoadServicesUseCase
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import com.ramonguimaraes.horacerta.utils.toLocalTime
import java.time.LocalTime
import java.util.Calendar

class GetAvailableHorsUseCase(
    private val repository: ScheduleRepository,
    private val scheduleConfigRepository: ScheduleConfigRepository
) {

    suspend fun getAvailableHors(calendar: Calendar, companyUid: String = "cY2YnxL8bBPLGj0GAjU8MTqFx213"): List<TimeInterval> {
        val scheduleConfig = getScheduleConfig(calendar, companyUid) ?: return listOf()

        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val result = mutableListOf<ScheduledTime>()
        repository.load(calendar.time).mapResourceSuccess {
            result.addAll(it)
        }
        val lista = geraLista(
            scheduleConfig.openTime,
            scheduleConfig.closeTime,
            scheduleConfig.intervalStart,
            scheduleConfig.intervalEnd,
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

    private suspend fun getScheduleConfig(calendar: Calendar, companyUid: String): ScheduleConfig? {
        var scheduleConfig: ScheduleConfig? = null
        val result = scheduleConfigRepository.getByDay(
            getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),
            companyUid
        )
        result.mapResourceSuccess {
            scheduleConfig = it
        }
        return scheduleConfig
    }

    private fun getDayOfWeek(day: Int): DayOfWeek {
        return when (day) {
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            else -> DayOfWeek.SATURDAY
        }
    }

    private fun geraLista(
        inicio: LocalTime,
        fim: LocalTime,
        intervalStart: LocalTime,
        intervalEnd: LocalTime,
        calendar: Calendar
    ): MutableList<TimeInterval> {
        val now = Calendar.getInstance()
        var horario = inicio
        var interval = intervalStart
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

        while (interval.isBefore(intervalEnd)) {
            list.removeIf {
                it.time == TimeInterval(interval).time
            }
            interval = interval.plusMinutes(INTERVAL)
        }

        return list
    }

    private companion object {
        private const val INTERVAL = 30L
    }
}
