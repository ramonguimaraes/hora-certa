package com.ramonguimaraes.horacerta

import com.ramonguimaraes.horacerta.domain.ValidationResult
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.AddressValidationUseCase
import org.junit.Test

class AddressValidationUseCaseTest {

    @Test
    fun `when street name is valid returns ValidationResult with successFul true`() {
        // given
        val useCase = AddressValidationUseCase()
        // when
        val result: ValidationResult = useCase.validateStreet("Rua Mampituba")
        // then
        assert(ValidationResult(successFul = true) == result)
    }

    @Test
    fun `when street name is null returns ValidationResult with successFul false and errorMessage equals campo obrigatorio`() {
        // given
        val useCase = AddressValidationUseCase()
        // when
        val result: ValidationResult = useCase.validateStreet(null)
        // then
        assert(ValidationResult(successFul = false, errorMessage = "campo obrigatorio") == result)
    }
}