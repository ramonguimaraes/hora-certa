package com.ramonguimaraes.horacerta.domain.companyProfile.model

import android.net.Uri

class CompanyProfile(
    var id: String = "",
    var companyUid: String = "",
    var companyName: String = "",
    var cnpj: String = "",
    var phoneNumber: String = "",
    var companySegment: String = "Saúde",
    var photoUri: Uri = Uri.EMPTY,
    var photoDownload: Uri = Uri.EMPTY,
    var rua: String = "",
    var bairro: String = "",
    var numero: String = "",
    var cidade: String = "",
    var complemento: String = "",
    var semNumero: Boolean = false,
    var latitude: Double? = null,
    var longitude: Double? = null
)

fun CompanyProfile.toHashMap(): HashMap<String, Any?> {
    return hashMapOf(
        "companyUid" to companyUid,
        "companyName" to companyName,
        "cnpj" to cnpj,
        "phoneNumber" to phoneNumber,
        "companySegment" to companySegment,
        "photoUri" to photoUri,
        "photoDownload" to photoDownload,
        "rua" to rua,
        "bairro" to bairro,
        "numero" to numero,
        "cidade" to cidade,
        "complemento" to complemento,
        "semNumero" to semNumero,
        "latitude" to latitude,
        "longitude" to longitude
    )
}
