package com.ramonguimaraes.horacerta.data.schedule.dataRepository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.toHashMap
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

class ScheduleRepositoryImpl(private val db: FirebaseFirestore) : ScheduleRepository {

    override suspend fun save(scheduledTimes: List<ScheduledTime>): Resource<Boolean?> {
        return try {
            scheduledTimes.forEach { scheduledTime ->
                db.collection(COLLECTION).add(scheduledTime.toHashMap()).await()
            }
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun load(date: Date): Resource<List<ScheduledTime>> {
        return try {
            val startTime = Timestamp(date)
            val endTime = Timestamp(Date(date.time + ONE_DAY_MINUS_ONE_MS))
            val scheduledTimesResult = mutableListOf<ScheduledTime>()
            val result = db.collection(COLLECTION)
                .whereGreaterThan("time", startTime)
                .whereLessThan("time", endTime).get().await()
            result.forEach { snapShot ->

                val uid = snapShot.get("uid", String::class.java)
                val companyUid = snapShot.get("companyUid", String::class.java)
                val userUid = snapShot.get("clientUid", String::class.java)
                val time = snapShot.get("time", Timestamp::class.java)
                val calenar = Calendar.getInstance()
                calenar.time = time?.toDate()!!

                scheduledTimesResult.add(ScheduledTime(uid!!, calenar, userUid!!, companyUid!!))
            }
            return Resource.Success(scheduledTimesResult)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private companion object {
        const val COLLECTION = "schedule"
        const val ONE_DAY_MINUS_ONE_MS = 86399999
        const val TAG = "scheduleRepositoryImpl"
    }
}
