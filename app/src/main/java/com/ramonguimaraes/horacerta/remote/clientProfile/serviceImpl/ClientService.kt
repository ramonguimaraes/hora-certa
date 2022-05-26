package com.ramonguimaraes.horacerta.remote.clientProfile.serviceImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.data.clientProfile.dataSource.ClientRemoteDataSource
import com.ramonguimaraes.horacerta.data.clientProfile.model.ClientData
import com.ramonguimaraes.horacerta.remote.ClientRemoteMapper
import com.ramonguimaraes.horacerta.remote.base.RemoteMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ClientService(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ClientRemoteDataSource {
    override suspend fun getClient(): ClientData {
        return ClientData("", "", "", "")
    }

    override suspend fun saveClient(client: ClientData): Boolean {
        val userId = auth.currentUser?.uid
        val docRef = db.collection(CLIENTS_COLLECTION).document(userId.toString())

        return try {
            suspendCoroutine { continuation ->
                docRef.set(client).addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(it.isSuccessful)
                    } else {
                        continuation.resumeWithException(Throwable(it.exception))
                    }
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log(CRASHLYTICS_KEY)
            FirebaseCrashlytics.getInstance().recordException(e)
            false
        }
    }

    companion object {
        const val CLIENTS_COLLECTION = "clients"
        const val CRASHLYTICS_KEY = "ClientService"
    }
}
