package com.ramonguimaraes.horacerta.domain.companyProfile.useCase

import com.ramonguimaraes.horacerta.domain.ValidationResult

class AddressValidationUseCase {

    fun validateStreet(street: String?): ValidationResult {
        return when {
            street.isNullOrBlank() -> ValidationResult(successFul = false, "campo obrigatorio")
            else -> ValidationResult(successFul = true)
        }
    }

    fun validateNumber(number: String?, withoutNumber: Boolean): ValidationResult {
        return when {
            number.isNullOrBlank() && !withoutNumber -> ValidationResult(
                successFul = false,
                "campo obrigatorio"
            )

            else -> ValidationResult(successFul = true)
        }
    }

    fun validateCity(city: String?): ValidationResult {
        return when {
            city.isNullOrBlank() -> ValidationResult(successFul = false, "campo obrigatorio")
            else -> ValidationResult(successFul = true)
        }
    }

    fun validateNeighborhood(neighborhood: String?): ValidationResult {
        return when {
            neighborhood.isNullOrBlank() -> ValidationResult(
                successFul = false,
                "campo obrigatorio"
            )

            else -> ValidationResult(successFul = true)
        }
    }
}