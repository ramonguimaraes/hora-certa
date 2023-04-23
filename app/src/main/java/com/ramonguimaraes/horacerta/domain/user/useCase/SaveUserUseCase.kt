package com.ramonguimaraes.horacerta.domain.user.useCase

import com.google.firebase.firestore.DocumentReference
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.user.model.User
import com.ramonguimaraes.horacerta.domain.user.repository.UserRepository

class SaveUserUseCase(private val repository: UserRepository) {

    suspend fun execute(
        documentReference: DocumentReference? = null,
        user: User
    ): Resource<DocumentReference?> {
        return if (documentReference != null) {
            repository.update(user, documentReference)
        } else {
            repository.save(user)
        }
     }
}
