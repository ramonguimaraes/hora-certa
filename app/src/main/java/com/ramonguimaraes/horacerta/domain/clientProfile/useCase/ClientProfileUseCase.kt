package com.ramonguimaraes.horacerta.domain.clientProfile.useCase

import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.clientProfile.model.ClientModel
import com.ramonguimaraes.horacerta.domain.clientProfile.repository.ClientProfileRepository
import kotlinx.coroutines.flow.flow

class ClientProfileUseCase(private val repository: ClientProfileRepository) {
    suspend fun saveClientProfile(clientModel: ClientModel) = flow {
        emit(Result.Loading)
        val result = repository.saveClient(clientModel)

        if (result) {
            emit(Result.Success("Perfil criado com sucesso"))
            return@flow
        }

        emit(Result.Failure("Falha ao criar perfil"))
        return@flow
    }
}
