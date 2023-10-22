package com.ramonguimaraes.horacerta.domain.schedule.repository

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ClientAppointment
import java.util.Date

interface ScheduleRepository {
    suspend fun save(appointment: Appointment, scheduledTimes: List<ScheduledTime>): Resource<Boolean>
    suspend fun loadSchedule(date: Date, companyUid: String): Resource<List<ScheduledTime>>
    suspend fun load(date: Date, companyUid: String): Resource<List<Appointment>>
    suspend fun load(clientUid: String): Resource<List<ClientAppointment>>
    suspend fun delete(appointment: ClientAppointment): Resource<Boolean>
}
