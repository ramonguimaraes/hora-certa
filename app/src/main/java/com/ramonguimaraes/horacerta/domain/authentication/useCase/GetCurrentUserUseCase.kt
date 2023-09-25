package com.ramonguimaraes.horacerta.domain.authentication.useCase

import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.user.repository.UserRepository

class GetCurrentUserUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = userRepository.load(authRepository.currentUser!!.uid)
    suspend fun getUserType() = userRepository.load(authRepository.currentUser!!.uid)
    fun currentUid() = authRepository.currentUser?.uid
    fun loggedUser(): Boolean {
        return authRepository.currentUser != null
    }
}
