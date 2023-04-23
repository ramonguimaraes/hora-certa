package com.ramonguimaraes.horacerta.domain.authentication.useCase

import com.google.firebase.auth.FirebaseUser
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class SingUpUseCase(
    private val authRepository: AuthRepository
) {

    suspend fun execute(email: String, password: String): Resource<FirebaseUser?> {
        return authRepository.singUp(email, password)
    }
}
