package com.ramonguimaraes.horacerta.data.scheduleConfig

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.toHashMap
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import kotlinx.coroutines.tasks.await
import java.time.LocalTime
import kotlin.Exception

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

    override suspend fun update(scheduleConfig: ScheduleConfig): Resource<ScheduleConfig> {
        return try {
            db.collection("ScheduleConfig").document(scheduleConfig.id).update(scheduleConfig.toHashMap())
            Resource.Success(scheduleConfig)
        } catch (e: Exception) {
            Log.e("ScheduleConfigRepositoryImpl", e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun delete(id: String): Resource<Boolean> {
        return try {
            db.collection("ScheduleConfig").document(id).delete().await()
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e("ScheduleConfigRepositoryImpl", e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun list(uid: String?): Resource<List<ScheduleConfig>> {
        return try {
            val list: MutableList<ScheduleConfig> = mutableListOf()
            val result =
                db.collection("ScheduleConfig").whereEqualTo("companyUid", uid).get().await()
            result.forEach { docSnapshot ->
                list.add(
                    ScheduleConfig(
                        id = docSnapshot.id,
                        companyUid = docSnapshot.get("companyUid", String::class.java)!!,
                        dayOfWeek = docSnapshot.get("dayOfWeek", DayOfWeek::class.java)!!,
                        openTime = LocalTime.parse(docSnapshot.get("openTime", String::class.java)!!),
                        intervalStart = LocalTime.parse(docSnapshot.get("intervalStart", String::class.java)!!),
                        intervalEnd = LocalTime.parse(docSnapshot.get("intervalEnd", String::class.java)!!),
                        closeTime = LocalTime.parse(docSnapshot.get("closeTime", String::class.java)!!)
                    )
                )
            }
            Resource.Success(list)
        } catch (e: Exception) {
            Log.e("ScheduleConfigRepositoryImpl", e.message.toString())
            Resource.Failure(e)
        }
    }
}
