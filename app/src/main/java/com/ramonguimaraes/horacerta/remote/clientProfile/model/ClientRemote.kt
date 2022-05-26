package com.ramonguimaraes.horacerta.remote.clientProfile.model

import android.os.Parcelable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import java.lang.Exception

@Parcelize
class ClientRemote(
    val uid: String,
    val name: String,
    val phone: String,
    val email: String
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toClient(): ClientRemote? {
            return try {
                val uid = getString("uid")!!
                val name = getString("name")!!
                val phone = getString("phone")!!
                val email = getString("email")!!

                ClientRemote(uid, name, email, phone)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().log("Error converting client")
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }
    }
}
