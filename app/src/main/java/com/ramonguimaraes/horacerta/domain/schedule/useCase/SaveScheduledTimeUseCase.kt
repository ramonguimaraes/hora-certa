package com.ramonguimaraes.horacerta.domain.schedule.useCase

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import java.util.Calendar

class SaveScheduledTimeUseCase(private val repository: ScheduleRepository) {

    suspend fun save(calendar: Calendar, timeNeeded: Int): Resource<Boolean?> {
        val intervals = timeNeeded / 30
        val scheduledTimes = mutableListOf<ScheduledTime>()
        for (i in 0 until intervals) {
            scheduledTimes.add(
                ScheduledTime(
                    "abcdefg",
                    (calendar.clone() as Calendar),
                    "123123",
                    "123321"
                )
            )
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 30)
        }
        return repository.save(scheduledTimes)
    }
}
