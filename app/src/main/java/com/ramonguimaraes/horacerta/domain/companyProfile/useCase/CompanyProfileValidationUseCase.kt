package com.ramonguimaraes.horacerta.domain.companyProfile.useCase

import com.ramonguimaraes.horacerta.domain.ValidationResult
import com.ramonguimaraes.horacerta.utils.isValid
import java.net.URI

class CompanyProfileValidationUseCase {

    fun nameValidation(companyName: String?): ValidationResult {
        return when {
            companyName.isNullOrBlank() -> {
                ValidationResult(successFul = false, errorMessage = "É necessário informar um nome")
            }

            else -> ValidationResult(successFul = true)
        }
    }

    fun cnpjValidation(cnpj: String?): ValidationResult {
        return when {
            cnpj.isNullOrBlank() -> {
                ValidationResult(successFul = false, errorMessage = "É necessário informar o CNPJ")
            }

            else -> ValidationResult(successFul = true)
        }
    }

    fun phoneValidation(phone: String?): ValidationResult {
        return when {
            phone.isNullOrBlank() -> {
                ValidationResult(successFul = false, errorMessage = "É necessário informar o telefone")
            }

            else -> ValidationResult(successFul = true)
        }
    }

    fun segmentValidation(segment: String?): ValidationResult {
        return when {
            segment.isNullOrBlank() -> {
                ValidationResult(successFul = false, errorMessage = "É necessário selecionar um segmento")
            }

            else -> ValidationResult(successFul = true)
        }
    }
}
