package com.ramonguimaraes.horacerta.presenter.companyProfile.model

import android.net.Uri

data class CompanyProfileView(
    var id: String = "",
    var companyUid: String = "",
    var companyName: String = "",
    var companyNameError: String? = null,
    var cnpj: String = "",
    var cnpjError: String? = null,
    var phoneNumber: String = "",
    var phoneNumberError: String? = null,
    var companySegment: String = "Saúde",
    var companySegmentError: String? = null,
    var photoUri: Uri = Uri.EMPTY
)
