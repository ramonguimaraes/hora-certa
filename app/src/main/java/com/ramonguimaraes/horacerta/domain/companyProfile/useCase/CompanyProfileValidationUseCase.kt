package com.ramonguimaraes.horacerta.domain.companyProfile.useCase

import com.ramonguimaraes.horacerta.domain.ValidationResult

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

            cnpj.length != 18 -> {
                ValidationResult(successFul = false, errorMessage = "CNPJ Invalido")
            }

            !validateCNPJ(cnpj) -> {
                ValidationResult(successFul = false, errorMessage = "CNPJ Invalido")
            }

            else -> ValidationResult(successFul = true)
        }
    }

    fun phoneValidation(phone: String?): ValidationResult {
        return when {
            phone.isNullOrBlank() -> {
                ValidationResult(
                    successFul = false,
                    errorMessage = "É necessário informar o telefone"
                )
            }

            phoneLengthValidation(phone) -> {
                ValidationResult(
                    successFul = false,
                    errorMessage = "Telefone invalido"
                )
            }

            else -> ValidationResult(successFul = true)
        }
    }

    private fun validateCNPJ(cnpj: String): Boolean {
        val cleanCNPJ = cnpj.replace("[^0-9]".toRegex(), "")
        val firstDigitRes = multiplier(5, cleanCNPJ.subSequence(0, 12))
        val secondDigitRes = multiplier(6, cleanCNPJ.subSequence(0, 13))

        val firstDigit = calcDigit(firstDigitRes)
        val secondDigit = calcDigit(secondDigitRes)

        return firstDigit == cleanCNPJ[12].toString().toInt() &&
                secondDigit == cleanCNPJ[13].toString().toInt()
    }

    private fun calcDigit(res: Int): Int {
        val sobra = res % 11
        val digit = if (sobra < 2) 0 else 11 - sobra
        return digit
    }

    private fun multiplier(mult: Int, cnpjWithOutDigits: CharSequence): Int {
        var mult = mult
        var res = 0
        cnpjWithOutDigits.forEach { char ->
            res += char.toString().toInt() * mult--
            if (mult == 1) {
                mult = 9
            }
        }
        return res
    }

    private fun phoneLengthValidation(phone: String): Boolean {
        val length = phone.replace("[^0-9]".toRegex(), "").length
        return length < 10 || length > 11
    }

    fun segmentValidation(segment: String?): ValidationResult {
        return when {
            segment.isNullOrBlank() -> {
                ValidationResult(
                    successFul = false,
                    errorMessage = "É necessário selecionar um segmento"
                )
            }

            else -> ValidationResult(successFul = true)
        }
    }
}
