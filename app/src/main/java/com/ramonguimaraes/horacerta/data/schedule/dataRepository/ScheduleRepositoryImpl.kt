package com.ramonguimaraes.horacerta.data.schedule.dataRepository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.toHashMap
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

class ScheduleRepositoryImpl(private val db: FirebaseFirestore) : ScheduleRepository {

    /*
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
    */

    override suspend fun save(
        appointment: Appointment,
        scheduledTimes: List<ScheduledTime>
    ): Resource<Boolean>? {
        return try {
            db.runTransaction { transition ->
                val docRef1 = db.collection(SCHEDULE_APPOINTMENT).document()
                transition.set(docRef1, appointment.toHashMap())
                scheduledTimes.forEach {
                    val docRef2 = db.collection(COLLECTION).document()
                    transition.set(docRef2, it.toHashMap())
                }
            }.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun loadSchedule(date: Date, companyUid: String): Resource<List<ScheduledTime>> {
        return try {
            val startTime = Timestamp(date)
            val endTime = Timestamp(Date(date.time + ONE_DAY_MINUS_ONE_MS))
            val scheduledTimesResult = mutableListOf<ScheduledTime>()
            val result = db.collection(COLLECTION)
                .whereGreaterThan("time", startTime)
                .whereLessThan("time", endTime)
                .whereEqualTo("companyUid", companyUid).get().await()
            result.forEach { snapShot ->

                val companyUid = snapShot.get("companyUid", String::class.java)
                val userUid = snapShot.get("clientUid", String::class.java)
                val time = snapShot.get("time", Timestamp::class.java)
                val calenar = Calendar.getInstance()
                calenar.time = time?.toDate()!!

                scheduledTimesResult.add(ScheduledTime(calenar, userUid!!, companyUid!!))
            }
            return Resource.Success(scheduledTimesResult)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun load(date: Date, companyUid: String): Resource<List<Appointment>> {
        return try {
            val res = db.collection(SCHEDULE_APPOINTMENT)
                .whereEqualTo("companyUid", companyUid)
                .whereEqualTo("date", Timestamp(date)).get().await()

            val map = res.map {
                val time = it.get("date", Timestamp::class.java)
                val calenar = Calendar.getInstance()
                calenar.time = time?.toDate()!!

                Appointment(
                    time = it.get("time", String::class.java) ?: "",
                    client = it.get("client", String::class.java) ?: "",
                    service = it.get("service", String::class.java) ?: "",
                    companyUid = it.get("companyUid", String::class.java) ?: "",
                    date = calenar
                )
            }

            return Resource.Success(map)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private companion object {
        const val COLLECTION = "schedule"
        const val SCHEDULE_APPOINTMENT = "ScheduleAppointment"
        const val ONE_DAY_MINUS_ONE_MS = 86399999
        const val TAG = "scheduleRepositoryImpl"
    }
}
