package com.ramonguimaraes.horacerta.data.services.dataRepository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ServiceItem
import com.ramonguimaraes.horacerta.domain.services.ServicesRepository
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ServicesRepositoryImpl(private val db: FirebaseFirestore) : ServicesRepository {
    override suspend fun load(companyUid: String): Resource<List<ServiceItem>> {
        return try {
            val services: MutableList<ServiceItem> = mutableListOf()
            val result = db.collection(COLLECTION).get().await()
            result.forEach { snapShot ->
                services.add(
                    ServiceItem(
                        title = snapShot.get("title", String::class.java)!!,
                        duration = snapShot.get("duration", Int::class.java)!!
                    )
                )
            }
            return Resource.Success(services)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private companion object {
        const val COLLECTION = "services"
        const val TAG = "servicesRepositoryImpl"
    }
}
