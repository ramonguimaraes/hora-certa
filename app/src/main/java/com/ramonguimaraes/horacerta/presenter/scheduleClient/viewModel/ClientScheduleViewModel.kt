package com.ramonguimaraes.horacerta.presenter.scheduleClient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ClientAppointment
import kotlinx.coroutines.launch
import java.util.Calendar

class ClientScheduleViewModel(
    private val repository: ScheduleRepository,
    private val userUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val appointments: MutableList<ClientAppointment> = mutableListOf()
    private val mClientSchedule = MutableLiveData<Resource<List<ClientAppointment>>>()
    val clientSchedule: LiveData<Resource<List<ClientAppointment>>> get() = mClientSchedule

    init {
        load()
    }

    fun load() {
        mClientSchedule.value?.mapResourceSuccess {
            appointments.addAll(it)
        }
        mClientSchedule.value = Resource.Loading
        viewModelScope.launch {
            userUseCase.currentUid()?.let {
                val resource = repository.load(it)
                mClientSchedule.postValue(resource)
            }
        }
    }

    fun sortList(list: List<ClientAppointment>?): List<ClientAppointment> {
        val sortedList = list?.sortedBy { it.date }

        var previousDate: Calendar? = null

        sortedList?.forEach { appointment ->
            if (previousDate == null || !isSameDay(appointment.date, previousDate)) {
                appointment.showDateLabel = true
            }
            previousDate = appointment.date
        }

        return sortedList ?: emptyList()
    }

    private fun isSameDay(date1: Calendar, date2: Calendar?): Boolean {
        if (date2 == null) return false
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH)
    }

    fun cancel(clientAppointment: ClientAppointment) {
        viewModelScope.launch {
            val result = repository.delete(clientAppointment)
            result.mapResourceSuccess {
                if (it) {
                    load()
                }
            }
        }
    }
}
