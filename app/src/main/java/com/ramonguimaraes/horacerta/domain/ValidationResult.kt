package com.ramonguimaraes.horacerta.domain

data class ValidationResult(
    val successFul: Boolean,
    val errorMessage: String? = null,
)
