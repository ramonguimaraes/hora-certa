package com.ramonguimaraes.horacerta.data.profile

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.utils.await
import com.ramonguimaraes.horacerta.domain.Client
import com.ramonguimaraes.horacerta.domain.ProfileRepository
import com.ramonguimaraes.horacerta.domain.toHashMap

class ProfileRepositoryImpl(private val db: FirebaseFirestore) : ProfileRepository {

    override suspend fun save(client: Client) {
        try {
            db.collection("client").add(client.toHashMap()).await()
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    private companion object {
        const val TAG = "profileRepositoryImpl"
    }
}
