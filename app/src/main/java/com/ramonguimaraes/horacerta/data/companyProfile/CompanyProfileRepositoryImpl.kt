package com.ramonguimaraes.horacerta.data.companyProfile

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.model.toHashMap
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource
import kotlinx.coroutines.tasks.await

class CompanyProfileRepositoryImpl(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : CompanyProfileRepository {

    override suspend fun save(companyProfile: CompanyProfile): Resource<Boolean> {
        return try {
            val downloadUir = uploadPhoto(companyProfile.photoUri, "profilepic")
            companyProfile.photoUri = downloadUir
            db.collection(COLLECTION).add(companyProfile.toHashMap()).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun update(companyProfile: CompanyProfile): Resource<Boolean> {
        return try {
            val downloadUir = uploadPhoto(companyProfile.photoUri, "profilepic")
            companyProfile.photoUri = downloadUir
            db.collection(COLLECTION).document(companyProfile.id).update(companyProfile.toHashMap())
            Resource.Success(true)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun load(uid: String?): Resource<CompanyProfile?> {
        return try {
            val querySnapshot = db.collection(COLLECTION).whereEqualTo("companyUid", uid).get().await().singleOrNull()
            val companyProfile: CompanyProfile? = querySnapshot?.let {
                CompanyProfile(
                    companyUid = it.get("companyUid", String::class.java)!!,
                    companyName = it.get("companyName", String::class.java) ?: "",
                    cnpj = it.get("cnpj", String::class.java) ?: "",
                    phoneNumber = it.get("phoneNumber", String::class.java) ?: "",
                    companySegment = it.get("companySegment", String::class.java) ?: "",
                    photoUri = it.get("photoUri", String::class.java)?.toUri() ?: Uri.EMPTY,
                )
            }
            Resource.Success(companyProfile)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    private suspend fun uploadPhoto(
        uriFile: Uri,
        fileName: String
    ): Uri {
        val ref = storage.getReference(fileName)
        val task = ref.putFile(uriFile)
        return generateUrlDownload(ref, task).await()
    }

    private fun generateUrlDownload(
        reference: StorageReference,
        task: StorageTask<UploadTask.TaskSnapshot>
    ): Task<Uri> {
        return task.continueWithTask { taskExecuted ->
            if (taskExecuted.isSuccessful) {
                reference.downloadUrl
            } else {
                taskExecuted.exception?.let { exception ->
                    throw exception
                }
            }
        }
    }

    companion object {
        const val TAG = "CompanyProfileRepositoryImpl"
        const val COLLECTION = "companyProfile"
    }
}
