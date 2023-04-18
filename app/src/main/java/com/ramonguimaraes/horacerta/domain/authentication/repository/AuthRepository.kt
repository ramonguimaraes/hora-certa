package com.ramonguimaraes.horacerta.domain.authentication.repository

import com.google.firebase.auth.FirebaseUser
import com.ramonguimaraes.horacerta.domain.resource.Resource

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser?>
    suspend fun singUp(name: String, email: String, password: String): Resource<FirebaseUser?>
    fun logout()
}
