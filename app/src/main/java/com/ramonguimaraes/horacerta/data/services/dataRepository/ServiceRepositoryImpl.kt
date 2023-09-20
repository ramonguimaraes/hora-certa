package com.ramonguimaraes.horacerta.data.services.dataRepository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.model.toHashMap
import com.ramonguimaraes.horacerta.domain.services.repository.ServiceRepository
import kotlinx.coroutines.tasks.await

class ServiceRepositoryImpl(private val db: FirebaseFirestore) : ServiceRepository {

    override suspend fun load(id: String): Resource<Service> {
        TODO("Not yet implemented")
    }

    override suspend fun loadAll(companyUid: String): Resource<List<Service>> {
        return try {
            val servicesList: MutableList<Service> = mutableListOf()
            val querySnapshot = db.collection(COLLECTION)
                .whereEqualTo(FIELD_COMPANY_UID, companyUid).get().await()

            querySnapshot.forEach { docSnapshot ->
                val service = docSnapshot.toObject(Service::class.java)
                service.id = docSnapshot.id
                servicesList.add(service)
            }

            Resource.Success(servicesList)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun save(service: Service) {
        try {
            db.collection(COLLECTION).add(service.toHashMap()).await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override suspend fun update(service: Service) {
        try {
            db.collection(COLLECTION).document(service.id).update(service.toHashMap()).await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override suspend fun delete(service: Service) {
        try {
            db.collection(COLLECTION).document(service.id).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        const val TAG = "serviceRepositoryImpl"
        const val COLLECTION = "services"
        const val FIELD_COMPANY_UID = "companyUid"
    }
}
