package com.ramonguimaraes.horacerta.data.companyProfile

import android.net.Uri
import android.net.http.HttpException
import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.model.toHashMap
import com.ramonguimaraes.horacerta.domain.companyProfile.repository.CompanyProfileRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class CompanyProfileRepositoryImpl(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : CompanyProfileRepository {

    override suspend fun save(companyProfile: CompanyProfile): Resource<Boolean> {
        return try {
            if (companyProfile.photoUri != Uri.EMPTY) {
                val downloadUir = uploadPhoto(companyProfile.photoUri, companyProfile.companyUid)
                companyProfile.photoUri = downloadUir
            }

            db.collection(COLLECTION).add(companyProfile.toHashMap()).await()
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun update(companyProfile: CompanyProfile): Resource<Boolean> {
        return try {
            if (companyProfile.photoUri != Uri.EMPTY) {
                val downloadUir = uploadPhoto(companyProfile.photoUri, companyProfile.companyUid)
                companyProfile.photoUri = downloadUir
            }
            db.collection(COLLECTION).document(companyProfile.id).update(companyProfile.toHashMap())
            Resource.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun load(uid: String?): Resource<CompanyProfile?> {
        return try {
            val querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("companyUid", uid).get().await().singleOrNull()
            val companyProfile: CompanyProfile? = querySnapshot?.let { snapShot ->
                CompanyProfile(
                    id = snapShot.id,
                    companyUid = snapShot.get("companyUid", String::class.java)!!,
                    companyName = snapShot.get("companyName", String::class.java) ?: "",
                    cnpj = snapShot.get("cnpj", String::class.java) ?: "",
                    phoneNumber = snapShot.get("phoneNumber", String::class.java) ?: "",
                    companySegment = snapShot.get("companySegment", String::class.java) ?: "",
                    photoUri = downloadImage(uid ?: ""),
                    rua = snapShot.get("rua", String::class.java) ?: "",
                    bairro = snapShot.get("bairro", String::class.java) ?: "",
                    numero = snapShot.get("numero", String::class.java) ?: "",
                    cidade = snapShot.get("cidade", String::class.java) ?: "",
                    complemento = snapShot.get("complemento", String::class.java) ?: "",
                    semNumero = snapShot.get("semNumero", Boolean::class.java) ?: false,
                    latitude = snapShot.get("latitude", Double::class.java),
                    longitude = snapShot.get("longitude", Double::class.java)
                )
            }
            Resource.Success(companyProfile)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun load(city: String, uf: String): Resource<List<CompanyProfile>> {
        return try {
            val querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("cidade", city)
                .whereEqualTo("uf", uf)
                .get().await()
            val companies = convertToCompaniesList(querySnapshot)
            if (companies.isEmpty()) {
                Resource.Failure(EmptyListException("Nenhuma empresa encontrada na sua região"))
            } else {
                Resource.Success(companies)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
            Resource.Failure(e)
        }
    }

    class EmptyListException(private val msg: String = "Lista vazia"): Exception() {
        override val message: String
            get() = msg
    }

    override suspend fun loadBySegment(segment: String, city: String, uf: String): Resource<List<CompanyProfile>> {
        return try {
            val querySnapshot = db.collection(COLLECTION)
                .whereEqualTo("companySegment", segment)
                .whereEqualTo("cidade", city)
                .whereEqualTo("uf", uf)
                .get().await()

            val companies = convertToCompaniesList(querySnapshot)
            return if (companies.isEmpty()) {
                Resource.Failure(EmptyListException("Nenhuma empresa encontrada na sua região"))
            } else {
                Resource.Success(companies)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())

            val exception = when(e) {
                is IOException -> IOException("Problema de conexão")
                else -> Exception("Erro desconhecido")
            }

            Resource.Failure(exception)
        }
    }

    private suspend fun convertToCompaniesList(querySnapshot: QuerySnapshot): MutableList<CompanyProfile> {
        val companies: MutableList<CompanyProfile> = mutableListOf()

        querySnapshot.forEach { snapShot ->
            val companyUid = snapShot.get("companyUid", String::class.java)!!
            val companyProfile = CompanyProfile(
                id = snapShot.id,
                companyUid = companyUid,
                companyName = snapShot.get("companyName", String::class.java) ?: "",
                cnpj = snapShot.get("cnpj", String::class.java) ?: "",
                phoneNumber = snapShot.get("phoneNumber", String::class.java) ?: "",
                companySegment = snapShot.get("companySegment", String::class.java) ?: "",
                photoUri = downloadImage(companyUid),
                rua = snapShot.get("rua", String::class.java) ?: "",
                bairro = snapShot.get("bairro", String::class.java) ?: "",
                numero = snapShot.get("numero", String::class.java) ?: "",
                cidade = snapShot.get("cidade", String::class.java) ?: "",
                complemento = snapShot.get("complemento", String::class.java) ?: "",
                semNumero = snapShot.get("semNumero", Boolean::class.java) ?: false,
                latitude = snapShot.get("latitude", Double::class.java),
                longitude = snapShot.get("longitude", Double::class.java)
            )
            companies.add(companyProfile)
        }

        return companies
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

    private suspend fun downloadImage(
        fileName: String
    ): Uri {
        val reference = storage.reference.child(fileName)
        val file = withContext(Dispatchers.IO) {
            File.createTempFile(fileName, SUFFIX)
        }

        return reference.getFile(file).continueWith {
            if (it.isSuccessful) {
                file.toUri()
            } else {
                it.exception?.let {
                    Log.e(TAG, "foto de perfil não encontrada - $fileName")
                }
                Uri.EMPTY
            }
        }.await()
    }

    companion object {
        const val TAG = "CompanyProfileRepositoryImpl"
        const val COLLECTION = "companyProfile"
        const val SUFFIX = ".jpeg"
    }
}
