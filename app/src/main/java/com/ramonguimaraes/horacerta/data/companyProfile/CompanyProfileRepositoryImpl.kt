package com.ramonguimaraes.horacerta.data.companyProfile

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.model.toHashMap
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource
import kotlinx.coroutines.tasks.await

class CompanyProfileRepositoryImpl(private val db: FirebaseFirestore) : CompanyProfileRepository {

    override suspend fun save(companyProfile: CompanyProfile) {
        try {
            db.collection(COLLECTION).add(companyProfile.toHashMap()).await()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override suspend fun update(companyProfile: CompanyProfile) {
        try {
            db.collection(COLLECTION).document(companyProfile.id).update(companyProfile.toHashMap())
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override suspend fun load(uid: String?): Resource<CompanyProfile?> {
        return try {
            val res = db.collection(COLLECTION).whereEqualTo("companyUid", uid).get().await()
            val companyProfile = res.toObjects(CompanyProfile::class.java).singleOrNull()
            Resource.Success(companyProfile)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    companion object {
        const val TAG = "CompanyProfileRepositoryImpl"
        const val COLLECTION = "companyProfile"
    }
}
