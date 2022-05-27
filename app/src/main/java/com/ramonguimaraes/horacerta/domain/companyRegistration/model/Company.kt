package com.ramonguimaraes.horacerta.domain.companyRegistration.model

class Company(
    val uuid: String,
    val companyName: String,
    val email:  String,
    var address: Address? = null,
    var addressRef: String? = null,
) {
    fun hashMap(): HashMap<String, String?> {

        return hashMapOf(
            "uuid" to uuid,
            "companyName" to companyName,
            "email" to email,
            "address" to addressRef
        )
    }
}
