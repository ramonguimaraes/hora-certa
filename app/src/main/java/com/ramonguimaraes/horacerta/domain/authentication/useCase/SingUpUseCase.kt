package com.ramonguimaraes.horacerta.domain.authentication.useCase

import com.google.firebase.auth.FirebaseUser
import com.ramonguimaraes.horacerta.domain.Client
import com.ramonguimaraes.horacerta.domain.ProfileRepository
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class SingUpUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) {

    suspend fun execute(name: String, email: String, password: String): Resource<FirebaseUser?> {
        val result = authRepository.singUp(name, email, password)
        if (result is Resource.Success) {
            result.result?.uid?.let { uid ->
                profileRepository.save(
                    Client(uid, name, email)
                )
            }
        }
        return result
    }
}
