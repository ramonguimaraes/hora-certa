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
    var photoUri: Uri = Uri.EMPTY,
    var rua: String = "",
    var ruaErro: String? = null,
    var bairro: String = "",
    var bairroErro: String? = null,
    var numero: String = "sem número",
    var numeroErro: String? = null,
    var cidade: String = "",
    var cidadeErro: String? = null,
    var uf: String = "",
    var ufErro: String? = null,
    var complemento: String = "",
    var complementoErro: String? = null,
    var semComplemento: Boolean = false,
    var semNumero: Boolean = false,
    var enderecoError: String? = null,
    var addressLine1: String = "Não informado",
    var addressLine2: String = ""
) {

    init {
        if (rua != "") {
            addressLine1 = "$rua, $numero"
        }

        if (bairro.isNotBlank() && bairro.isNotBlank()) {
            addressLine2 = "$bairro. $cidade" + complemento.ifBlank { "." }
        }
    }
}
