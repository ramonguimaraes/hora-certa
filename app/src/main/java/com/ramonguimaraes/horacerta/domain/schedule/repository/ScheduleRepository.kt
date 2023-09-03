package com.ramonguimaraes.horacerta.domain.schedule.repository

import com.google.firebase.firestore.DocumentReference
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import java.util.Date

interface ScheduleRepository {
    suspend fun save(scheduledTime: ScheduledTime): Resource<DocumentReference?>
    suspend fun load(date: Date): Resource<List<ScheduledTime>>
}
