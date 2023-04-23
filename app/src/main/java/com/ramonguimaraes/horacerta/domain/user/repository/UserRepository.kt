package com.ramonguimaraes.horacerta.domain.user.repository

import com.google.firebase.firestore.DocumentReference
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.user.model.User

interface UserRepository {
    suspend fun save(user: User): Resource<DocumentReference?>
    suspend fun update(user: User, documentReference: DocumentReference): Resource<DocumentReference>
}
