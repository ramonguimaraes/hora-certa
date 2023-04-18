package com.ramonguimaraes.horacerta.domain.authentication.useCase

import com.google.firebase.auth.FirebaseUser
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class SingUpUseCase(private val repository: AuthRepository) {

    suspend fun execute(name: String, email: String, password: String): Resource<FirebaseUser?> {
        return repository.singUp(name, email, password)
    }
}
