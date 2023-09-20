package com.ramonguimaraes.horacerta.presenter.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.userCase.DeleteServiceUseCase
import com.ramonguimaraes.horacerta.domain.services.userCase.SaveServiceUseCase
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val saveServiceUseCase: SaveServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase
) : ViewModel() {

    val service = MutableLiveData(Service())
    val dismiss = MutableLiveData(false)
    var isUpdating : Boolean = false

    fun save() {
        viewModelScope.launch {
            service.value?.let { saveServiceUseCase(it) }
        }.invokeOnCompletion { dismiss() }
    }

    fun delete() {
        viewModelScope.launch {
            service.value?.let { deleteServiceUseCase(it) }
        }.invokeOnCompletion { dismiss() }
    }

    private fun dismiss() {
        dismiss.value = true
    }
}
