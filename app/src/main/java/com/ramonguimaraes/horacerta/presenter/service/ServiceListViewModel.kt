package com.ramonguimaraes.horacerta.presenter.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.userCase.LoadServicesUseCase
import kotlinx.coroutines.launch

class ServiceListViewModel(
    private val loadServicesUseCase: LoadServicesUseCase,
    userUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val mServiceList = MutableLiveData<Resource<List<Service>>?>()
    val serviceList: MutableLiveData<Resource<List<Service>>?> get() = mServiceList
    private val companyUid: String = userUseCase.currentUid().toString()

    init {
        loadAllServices()
    }

    private fun loadAllServices() {
        mServiceList.value = Resource.Loading
        viewModelScope.launch {
            val result = loadServicesUseCase(companyUid)
            mServiceList.postValue(result)
        }
    }
}
