package com.ramonguimaraes.horacerta.data.authentication.dataRepository

import android.util.Log
import com.google.firebase.FirebaseError.ERROR_OPERATION_NOT_ALLOWED
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.ramonguimaraes.horacerta.await
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result?.user?.isEmailVerified == true) {
                Resource.Success(result.user)
            } else {
                throw FirebaseAuthInvalidUserException(
                    ERROR_OPERATION_NOT_ALLOWED.toString(),
                    "email não verificado"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun singUp(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser?> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()
            firebaseAuth.signOut()
            Resource.Success(result.user)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    private companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}
