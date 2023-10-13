package com.ramonguimaraes.horacerta.presenter.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.utils.onlyDate
import kotlinx.coroutines.launch
import java.util.Calendar

class ScheduleViewModel(
    private val repository: ScheduleRepository,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val companyUid = getCurrentUserUseCase.currentUid()

    private val mSelectedDate = MutableLiveData(Calendar.getInstance().onlyDate())
    val selectedDate: LiveData<Calendar> get() = mSelectedDate

    private val mAppointments = MutableLiveData<Resource<List<Appointment>>>()
    val appointment: LiveData<Resource<List<Appointment>>> get() = mAppointments

    private val mShowDatePickerEvent = MutableLiveData(false)
    val showDatePickerEvent: LiveData<Boolean> get() = mShowDatePickerEvent

    private val mOpenScheduleRegistration = MutableLiveData(false)
    val openScheduleRegistration: LiveData<Boolean> get() = mOpenScheduleRegistration

    init {
        selectedDate.value?.let { load(it) }
    }

    fun load(onlyDate: Calendar) {
        mAppointments.value = Resource.Loading
        viewModelScope.launch {
            if (companyUid != null) {
                mAppointments.postValue(
                    repository.load(onlyDate.time, companyUid)
                )
            }
        }
    }

    fun setSelectedDate(calendar: Calendar) {
        mSelectedDate.value = calendar.onlyDate()
    }

    fun showDatePicker() {
        mShowDatePickerEvent.value = true
        mShowDatePickerEvent.value = false
    }

    fun openScheduleRegistration() {
        mOpenScheduleRegistration.value = true
        mOpenScheduleRegistration.value = false
    }
}
