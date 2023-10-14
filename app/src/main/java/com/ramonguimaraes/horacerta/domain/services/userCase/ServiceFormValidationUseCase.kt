package com.ramonguimaraes.horacerta.domain.services.userCase

import com.ramonguimaraes.horacerta.domain.ValidationResult
import com.ramonguimaraes.horacerta.utils.extensions.isValid

class ServiceFormValidationUseCase {

    fun titleValidation(title: String?): ValidationResult {
        return when {
            !title.isValid() -> {
                ValidationResult(
                    successFul = false,
                    errorMessage = "Titulo não pode ser vazio"
                )
            }

            else -> ValidationResult(successFul = true)
        }
    }

    fun priceValidation(price: Double?): ValidationResult {
        return when {
            price?.isNaN() == true -> {
                ValidationResult(
                    successFul = false,
                    errorMessage = "Valor invalido"
                )
            }

            else -> ValidationResult(successFul = true)
        }
    }

    fun durationValidation(duration: Long?): ValidationResult {
        return when {
            duration != null && duration <= 0L -> {
                ValidationResult(
                    successFul = false,
                    errorMessage = "Tempo de duração invalido"
                )
            }

            else -> ValidationResult(successFul = true)
        }
    }
}
