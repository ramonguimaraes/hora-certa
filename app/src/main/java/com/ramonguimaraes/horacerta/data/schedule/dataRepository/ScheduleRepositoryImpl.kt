package com.ramonguimaraes.horacerta.data.schedule.dataRepository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.toHashMap
import com.ramonguimaraes.horacerta.domain.schedule.model.toTimeStamp
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.services.model.Service
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

class ScheduleRepositoryImpl(private val db: FirebaseFirestore) : ScheduleRepository {

    override suspend fun save(
        appointment: Appointment,
        scheduledTimes: List<ScheduledTime>
    ): Resource<Boolean>? {
        return try {
            db.runTransaction { transaction ->
                val scheduledTimesReferences = mutableListOf<DocumentReference>()
                scheduledTimes.forEach { scheduledTime ->
                    val scheduledTimeReference = db.collection(COLLECTION).document()
                    scheduledTime.id = scheduledTimeReference.id
                    transaction.set(scheduledTimeReference, scheduledTime.toHashMap())
                    scheduledTimesReferences.add(scheduledTimeReference)
                }

                val appointmentReference = db.collection(SCHEDULE_APPOINTMENT).document()
                transaction.set(
                    appointmentReference,
                    appointmentHashMap(appointment, scheduledTimesReferences)
                )
            }.await()
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private fun appointmentHashMap(
        appointment: Appointment,
        scheduledTimes: List<DocumentReference>
    ): HashMap<String, Any> {
        return hashMapOf(
            "scheduledTimes" to scheduledTimes.map { it.id },
            "services" to appointment.services.map { it.id },
            "companyUid" to appointment.companyUid,
            "clientUid" to appointment.clientUid,
            "clientName" to appointment.clientName,
            "date" to appointment.date.toTimeStamp()
        )
    }

    override suspend fun loadSchedule(
        date: Date,
        companyUid: String
    ): Resource<List<ScheduledTime>> {
        return try {
            val startTime = Timestamp(date)
            val endTime = Timestamp(Date(date.time + ONE_DAY_MINUS_ONE_MS))
            val scheduledTimesResult = mutableListOf<ScheduledTime>()
            val result = db.collection(COLLECTION)
                .whereGreaterThan("time", startTime)
                .whereLessThan("time", endTime)
                .whereEqualTo("companyUid", companyUid).get().await()
            result.forEach { snapShot ->
                scheduledTimesResult.add(resolveScheduledTime(snapShot))
            }
            return Resource.Success(scheduledTimesResult)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private fun resolveScheduledTime(snapShot: QueryDocumentSnapshot): ScheduledTime {
        val id = snapShot.get("id", String::class.java)
        val companyUid = snapShot.get("companyUid", String::class.java)
        val userUid = snapShot.get("clientUid", String::class.java)
        val time = snapShot.get("time", Timestamp::class.java)
        val calenar = Calendar.getInstance()
        calenar.time = time?.toDate()!!

        return ScheduledTime(
            id = id ?: "",
            time = calenar,
            clientUid = userUid ?: "",
            companyUid = companyUid ?: ""
        )
    }

    override suspend fun load(date: Date, companyUid: String): Resource<List<Appointment>> {
        return try {
            val endDate = Date(date.time + ONE_DAY_MINUS_ONE_MS)
            val res = db.collection(SCHEDULE_APPOINTMENT)
                .whereEqualTo("companyUid", companyUid)
                .whereGreaterThan("date", Timestamp(date))
                .whereLessThan("date", Timestamp(endDate))
                .get().await()

            val map = res.map {
                val time = it.get("date", Timestamp::class.java)
                val calendar = Calendar.getInstance()
                calendar.time = time?.toDate()!!

                Appointment(
                    scheduledTimes = loadSchedules(it),
                    services = loadServices(it),
                    companyUid = it.get("companyUid", String::class.java) ?: "",
                    clientUid = it.get("companyUid", String::class.java) ?: "",
                    clientName = it.get("clientName", String::class.java) ?: "",
                    date = calendar
                )
            }

            return Resource.Success(map)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private suspend fun loadServices(queryDocumentSnapshot: QueryDocumentSnapshot): List<Service> {
        val servicesRefs = queryDocumentSnapshot.get("services") as List<*>
        val servicesRes = db.collection(SERVICES).whereIn("id", servicesRefs).get().await()
        val services = mutableListOf<Service>()
        servicesRes.forEach { snapShot ->
            val service = snapShot.toObject(Service::class.java)
            service.id = snapShot.id
            services.add(service)
        }
        return services
    }

    private suspend fun loadSchedules(queryDocumentSnapshot: QueryDocumentSnapshot): List<ScheduledTime> {
        val ids = queryDocumentSnapshot.get("scheduledTimes") as List<*>
        val scheduledTimes = mutableListOf<ScheduledTime>()
        val scheduledTimesRes = db.collection(COLLECTION).whereIn("id", ids).get().await()
        scheduledTimesRes.forEach { snapShot ->
            scheduledTimes.add(resolveScheduledTime(snapShot))
        }
        return scheduledTimes
    }

    private companion object {
        const val COLLECTION = "schedule"
        const val SCHEDULE_APPOINTMENT = "ScheduleAppointment"
        const val SERVICES = "services"
        const val ONE_DAY_MINUS_ONE_MS = 86399999
        const val TAG = "scheduleRepositoryImpl"
    }
}
