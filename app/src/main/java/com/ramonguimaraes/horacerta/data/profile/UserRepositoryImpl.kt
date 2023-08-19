package com.ramonguimaraes.horacerta.data.profile

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.user.model.User
import com.ramonguimaraes.horacerta.domain.user.model.toHashMap
import com.ramonguimaraes.horacerta.domain.user.repository.UserRepository
import com.ramonguimaraes.horacerta.utils.AccountType
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val db: FirebaseFirestore) : UserRepository {

    override suspend fun save(user: User): Resource<DocumentReference?> {
        return try {
            val result = db.collection(DOCUMENT)
                .add(user.toHashMap()).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun update(
        user: User,
        documentReference: DocumentReference
    ): Resource<DocumentReference> {
        return try {
            // POR ALGUM MOTIVO UM SIMPLES UPDATE NÃO FUNCIONOU
            // TODO : VER ISSO DEPOIS.
            db.runTransaction {
                it.update(documentReference, "uid", user.uid)
                return@runTransaction
            }.await()
            Resource.Success(documentReference)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    override suspend fun load(uid: String): Resource<User?> {
        return try {
            val result = db.collection(DOCUMENT).whereEqualTo("uid", uid).get().await()
            val user = result.toObjects(User::class.java).single()

            Resource.Success(user)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Resource.Failure(e)
        }
    }

    private fun getAccountTypeFromValue(value: Int): AccountType {
        return when (value) {
            AccountType.CLIENT.type -> AccountType.CLIENT
            AccountType.COMPANY.type -> AccountType.COMPANY
            else -> AccountType.NONE
        }
    }

    private companion object {
        const val DOCUMENT = "user"
        const val TAG = "profileRepositoryImpl"
    }
}
