package com.ramonguimaraes.horacerta.data.scheduleConfig

import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository

class ScheduleConfigRepositoryImpl(private val db: FirebaseFirestore): ScheduleConfigRepository {

    override suspend fun save(scheduleConfig: ScheduleConfig): Resource<ScheduleConfig> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun list(): Resource<List<ScheduleConfig>> {
        TODO("Not yet implemented")
    }
}
