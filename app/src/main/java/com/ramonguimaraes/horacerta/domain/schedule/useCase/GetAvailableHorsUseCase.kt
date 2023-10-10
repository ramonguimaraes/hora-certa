package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import com.ramonguimaraes.horacerta.utils.toLocalTime
import java.time.LocalTime
import java.util.Calendar

class GetAvailableHorsUseCase(
    private val repository: ScheduleRepository,
    private val scheduleConfigRepository: ScheduleConfigRepository
) {

    suspend fun getAvailableHors(
        calendar: Calendar,
        companyUid: String,
    ): List<TimeInterval> {
        val scheduleConfig = getScheduleConfig(calendar, companyUid) ?: return listOf()

        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val unavailableTimes = mutableListOf<ScheduledTime>()
        repository.load(calendar.time).mapResourceSuccess {
            unavailableTimes.addAll(it)
        }

        val allHors = generateAllHors(
            scheduleConfig.openTime,
            scheduleConfig.closeTime,
            calendar
        )

        val availableHours = setIntervals(
            scheduleConfig.intervalStart,
            scheduleConfig.intervalEnd,
            allHors
        )

        unavailableTimes.forEach { scheduled ->
            val localTime = LocalTime.of(
                scheduled.time.get(Calendar.HOUR_OF_DAY),
                scheduled.time.get(Calendar.MINUTE)
            )

            availableHours.forEach { interval ->
                if (interval.time == localTime) {
                    interval.disponivel = false
                    interval.show = false
                }
            }
        }

        return availableHours
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

    private fun generateAllHors(
        openHour: LocalTime,
        closeHour: LocalTime,
        calendar: Calendar
    ): MutableList<TimeInterval> {
        val now = Calendar.getInstance()
        var openHourAux = openHour
        val hours = mutableListOf<TimeInterval>()

        if (now.time.before(calendar.time)) {
            openHourAux = LocalTime.of(8, 0, 0)
        } else {
            while (openHourAux.isBefore(now.toLocalTime())) {
                openHourAux = openHourAux.plusMinutes(INTERVAL)
            }
        }

        while (openHourAux.isBefore(closeHour)) {
            hours.add(TimeInterval(openHourAux))
            openHourAux = openHourAux.plusMinutes(INTERVAL)
        }

        return hours
    }

    private fun setIntervals(
        intervalStart: LocalTime,
        intervalEnd: LocalTime,
        allHors: MutableList<TimeInterval>
    ): MutableList<TimeInterval> {
        var interval = intervalStart
        while (interval.isBefore(intervalEnd)) {
            allHors.forEach {
                if (it.time == TimeInterval(interval).time) {
                    it.show = false
                    it.disponivel = false
                }
            }
            interval = interval.plusMinutes(INTERVAL)
        }

        return allHors
    }

    private companion object {
        private const val INTERVAL = 30L
    }
}
