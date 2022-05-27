package com.ramonguimaraes.horacerta.presentation.clientProfile.viewmode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.clientProfile.useCase.ClientProfileUseCase
import com.ramonguimaraes.horacerta.presentation.clientProfile.mapper.ClientViewMapper
import com.ramonguimaraes.horacerta.presentation.clientProfile.model.ClientView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientProfileViewModel(
    private val clientProfileUseCase: ClientProfileUseCase,
    private val mapper: ClientViewMapper
) : ViewModel() {

    private val mResult = MutableLiveData<Result<String>>()
    val result: LiveData<Result<String>>
        get() = mResult

    fun saveClient(client: ClientView) {
        val mappedClient = mapper.fromView(client)
        viewModelScope.launch(Dispatchers.IO) {
            clientProfileUseCase.saveClientProfile(mappedClient).collect {
                withContext(Dispatchers.Main) {
                    mResult.postValue(it)
                }
            }
        }
    }
}
