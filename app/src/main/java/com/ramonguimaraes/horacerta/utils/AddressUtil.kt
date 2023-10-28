package com.ramonguimaraes.horacerta.utils

import android.location.Address

object AddressUtil {
    private val stateAbbreviations = mapOf(
        "Acre" to "AC",
        "Alagoas" to "AL",
        "Amapá" to "AP",
        "Amazonas" to "AM",
        "Bahia" to "BA",
        "Ceará" to "CE",
        "Distrito Federal" to "DF",
        "Espírito Santo" to "ES",
        "Goiás" to "GO",
        "Maranhão" to "MA",
        "Mato Grosso" to "MT",
        "Mato Grosso do Sul" to "MS",
        "Minas Gerais" to "MG",
        "Pará" to "PA",
        "Paraíba" to "PB",
        "Paraná" to "PR",
        "Pernambuco" to "PE",
        "Piauí" to "PI",
        "Rio de Janeiro" to "RJ",
        "Rio Grande do Norte" to "RN",
        "Rio Grande do Sul" to "RS",
        "Rondônia" to "RO",
        "Roraima" to "RR",
        "Santa Catarina" to "SC",
        "São Paulo" to "SP",
        "Sergipe" to "SE",
        "Tocantins" to "TO"
    )

    fun getStateAbbreviation(address: Address?): String? {
        val state = address?.adminArea
        return stateAbbreviations[state]
    }
}