package com.ramonguimaraes.horacerta.presenter.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.userCase.SaveServiceUseCase
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val saveServiceUseCase: SaveServiceUseCase
) : ViewModel() {

    val service = MutableLiveData(Service())
    val dismiss = MutableLiveData(false)

    fun save() {
        viewModelScope.launch {
            service.value?.let { saveServiceUseCase(it) }
        }.invokeOnCompletion { dismiss() }
    }

    private fun dismiss() {
        dismiss.value = true
    }
}
