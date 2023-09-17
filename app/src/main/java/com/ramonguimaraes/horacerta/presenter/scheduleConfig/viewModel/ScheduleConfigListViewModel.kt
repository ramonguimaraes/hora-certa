package com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase.ScheduleConfigListUseCase
import kotlinx.coroutines.launch

class ScheduleConfigListViewModel(
    private val scheduleConfigListUseCase: ScheduleConfigListUseCase
) : ViewModel() {

    private val mScheduleConfigList = MutableLiveData<Resource<List<ScheduleConfig>>>()
    val scheduleConfigList: LiveData<Resource<List<ScheduleConfig>>> get() = mScheduleConfigList

    init {
        loadScheduleConfigList()
    }

    fun loadScheduleConfigList() {
        mScheduleConfigList.postValue(Resource.Loading)
        viewModelScope.launch { mScheduleConfigList.postValue(scheduleConfigListUseCase.invoke()) }
    }

    fun verifyListSize(): Boolean {
        val list = scheduleConfigList.value?.getResultData()
        return list?.let {
            it.size >= 7
        } == true
    }
}
