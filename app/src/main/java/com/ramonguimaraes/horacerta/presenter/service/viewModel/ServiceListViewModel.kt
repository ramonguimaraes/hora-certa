package com.ramonguimaraes.horacerta.presenter.service.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.services.userCase.LoadServicesUseCase
import com.ramonguimaraes.horacerta.presenter.service.model.ServiceView
import com.ramonguimaraes.horacerta.presenter.service.viewMapper.ServiceViewMapper
import kotlinx.coroutines.launch

class ServiceListViewModel(
    private val loadServicesUseCase: LoadServicesUseCase,
    private val serviceViewMapper: ServiceViewMapper,
    userUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val mServiceList = MutableLiveData<Resource<List<ServiceView>>?>()
    val serviceList: MutableLiveData<Resource<List<ServiceView>>?> get() = mServiceList
    private val companyUid: String = userUseCase.currentUid().toString()

    init {
        loadAllServices()
    }

    fun loadAllServices() {
        mServiceList.value = Resource.Loading
        viewModelScope.launch {
            val result = loadServicesUseCase(companyUid).mapResourceSuccess {
                serviceViewMapper.mapToView(it)
            }
            mServiceList.postValue(result)
        }
    }
}
