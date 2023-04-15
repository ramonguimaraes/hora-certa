package com.ramonguimaraes.horacerta

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser?>
    suspend fun singUp(name: String, email: String, password: String): Resource<FirebaseUser?>
    fun logout()
}
