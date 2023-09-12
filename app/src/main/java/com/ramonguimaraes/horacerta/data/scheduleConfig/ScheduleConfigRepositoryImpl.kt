package com.ramonguimaraes.horacerta.data.scheduleConfig

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.toHashMap
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.time.LocalTime

class ScheduleConfigRepositoryImpl(private val db: FirebaseFirestore) : ScheduleConfigRepository {

    override suspend fun save(scheduleConfig: ScheduleConfig): Resource<ScheduleConfig> {
        return try {
            db.collection("ScheduleConfig").add(scheduleConfig.toHashMap())
            Resource.Success(scheduleConfig)
        } catch (e: Exception) {
            Log.e("ScheduleConfigRepositoryImpl", e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun delete(id: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun list(): Resource<List<ScheduleConfig>> {
        TODO("Not yet implemented")
    }
}
