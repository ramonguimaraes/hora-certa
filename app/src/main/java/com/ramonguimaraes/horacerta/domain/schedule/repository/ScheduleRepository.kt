package com.ramonguimaraes.horacerta.domain.schedule.repository

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.ui.schedule.adapter.Appointment
import java.util.Date

interface ScheduleRepository {
    // suspend fun save(scheduledTimes: List<ScheduledTime>): Resource<Boolean?>
    suspend fun save(appointment: Appointment, scheduledTimes: List<ScheduledTime>): Resource<Boolean>?
    suspend fun load(date: Date): Resource<List<ScheduledTime>>
    suspend fun load(date: Date, companyUid: String): Resource<List<Appointment>>
}
