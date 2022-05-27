package com.ramonguimaraes.horacerta.domain.companyRegistration.model

import android.os.Parcelable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
class Address(
    val country: String,
    val state: String,
    val city: String,
    val district: String,
    val street: String,
    val number: String,
    val zipCode: String,
    val latitude: String,
    val longitude: String,
    val complement: String
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toAddress(): Address? {
            return try {
                val country = getString("country")!!
                val state = getString("state")!!
                val city = getString("city")!!
                val district = getString("district")!!
                val street = getString("street")!!
                val number = getString("number")!!
                val zipCode = getString("zipCode")!!
                val latitude = getString("latitude")!!
                val longitude = getString("longitude")!!
                val complement = getString("complement")!!

                Address(
                    country,
                    state,
                    city,
                    district,
                    street,
                    number,
                    zipCode,
                    latitude,
                    longitude,
                    complement
                )
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().log("Error converting client")
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }
    }

    override fun toString(): String {
        return "$street, N° $number, Bairro $district. $city - $state. $country"
    }
}
