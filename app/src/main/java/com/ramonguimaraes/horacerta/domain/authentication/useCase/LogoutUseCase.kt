package com.ramonguimaraes.horacerta.domain.authentication.useCase

import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    operator fun invoke() {
        authRepository.logout()
    }
}