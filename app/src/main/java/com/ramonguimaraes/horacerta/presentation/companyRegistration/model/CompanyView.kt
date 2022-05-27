package com.ramonguimaraes.horacerta.presentation.companyRegistration.model

import com.ramonguimaraes.horacerta.presentation.companyRegistration.model.AddressView

class CompanyView (
    val uuid: String,
    val companyName: String,
    val email:  String,
    var address: AddressView? = null,
)
