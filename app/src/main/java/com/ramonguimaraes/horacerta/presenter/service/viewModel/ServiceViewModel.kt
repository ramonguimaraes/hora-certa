package com.ramonguimaraes.horacerta.presenter.service.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.services.userCase.DeleteServiceUseCase
import com.ramonguimaraes.horacerta.domain.services.userCase.SaveServiceUseCase
import com.ramonguimaraes.horacerta.domain.services.userCase.ServiceFormValidationUseCase
import com.ramonguimaraes.horacerta.presenter.service.model.ServiceView
import com.ramonguimaraes.horacerta.presenter.service.viewMapper.ServiceViewMapper
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val saveServiceUseCase: SaveServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    private val serviceFormValidation: ServiceFormValidationUseCase,
    private val serviceViewMapper: ServiceViewMapper
) : ViewModel() {

    val serviceView = MutableLiveData(ServiceView())
    val dismiss = MutableLiveData(false)
    var isUpdating: Boolean = false

    private fun formValidation(): Boolean {
        val titleResult = serviceFormValidation.titleValidation(serviceView.value?.title)
        val priceResult = serviceFormValidation.priceValidation(serviceView.value?.price)
        val durationResult = serviceFormValidation.durationValidation(serviceView.value?.duration)

        serviceView.value = serviceView.value?.copy(
            titleError = titleResult.errorMessage,
            priceError = priceResult.errorMessage,
            durationError = durationResult.errorMessage
        )

        return listOf(
            titleResult,
            priceResult,
            durationResult
        ).all { it.successFul }
    }

    fun save() {
        if (formValidation()) {
            val view = serviceView.value
            val model = view?.let { serviceViewMapper.mapFromView(it) }
            viewModelScope.launch {
                if (model != null) {
                    saveServiceUseCase(model)
                }
            }.invokeOnCompletion { dismiss() }
        }
    }

    fun delete() {
        viewModelScope.launch {
            serviceView.value?.let { serviceView ->
                deleteServiceUseCase(serviceViewMapper.mapFromView(serviceView))
            }
        }.invokeOnCompletion { dismiss() }
    }

    private fun dismiss() {
        dismiss.value = true
    }
}
