package com.ramonguimaraes.horacerta.data.scheduleConfig

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.toHashMap
import com.ramonguimaraes.horacerta.domain.scheduleConfig.repository.ScheduleConfigRepository
import com.ramonguimaraes.horacerta.domain.schedule.model.DayOfWeek
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
                    convertToScheduleConfig(docSnapshot)
                )
            }
            Resource.Success(list)
        } catch (e: Exception) {
            Log.e("ScheduleConfigRepositoryImpl", e.message.toString())
            Resource.Failure(e)
        }
    }

    private fun convertToScheduleConfig(snapShot: QueryDocumentSnapshot) : ScheduleConfig {
        return ScheduleConfig(
            id = snapShot.id,
            companyUid = snapShot.get("companyUid", String::class.java)!!,
            dayOfWeek = snapShot.get("dayOfWeek", DayOfWeek::class.java)!!,
            openTime = LocalTime.parse(snapShot.get("openTime", String::class.java)!!),
            intervalStart = LocalTime.parse(snapShot.get("intervalStart", String::class.java)!!),
            intervalEnd = LocalTime.parse(snapShot.get("intervalEnd", String::class.java)!!),
            closeTime = LocalTime.parse(snapShot.get("closeTime", String::class.java)!!)
        )
    }

    override suspend fun getByDay(day: DayOfWeek, uid: String): Resource<ScheduleConfig?> {
        return try {
            val res = db.collection("ScheduleConfig").whereEqualTo("companyUid", uid).whereEqualTo("dayOfWeek", day).get().await()
            val scheduleConfig = res.firstOrNull()?.let {
                convertToScheduleConfig(it)
            }
            Resource.Success(scheduleConfig)
        } catch (e: Exception) {
            Log.e("ScheduleConfigRepositoryImpl", e.message.toString())
            Resource.Failure(e)
        }
    }
}
