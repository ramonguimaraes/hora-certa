package com.ramonguimaraes.horacerta.domain.companyProfile.model

class CompanyProfile(
    var id: String = "",
    var companyUid: String = "",
    var companyName: String = "",
    var cnpj: String = "",
    var phoneNumber: String = "",
    var companySegment: String = "Saúde"
)

fun CompanyProfile.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "companyUid" to companyUid,
        "companyName" to companyName,
        "cnpj" to cnpj,
        "phoneNumber" to phoneNumber,
        "companySegment" to companySegment
    )
}
