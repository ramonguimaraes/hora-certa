package com.ramonguimaraes.horacerta.domain.scheduleConfig.repository

import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek

interface ScheduleConfigRepository {
    suspend fun save(scheduleConfig: ScheduleConfig): Resource<ScheduleConfig>
    suspend fun update(scheduleConfig: ScheduleConfig): Resource<ScheduleConfig>
    suspend fun delete(id: String): Resource<Boolean>
    suspend fun list(uid: String?): Resource<List<ScheduleConfig>>
    suspend fun getByDay(day: DayOfWeek, uid: String): Resource<ScheduleConfig?>
}
