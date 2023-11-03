package com.ramonguimaraes.horacerta.presenter.schedule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.cashFlow.CashRegister
import com.ramonguimaraes.horacerta.domain.cashFlow.repository.CashFlowRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.model.Appointment
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.presenter.cashFlow.model.PaymentMethod
import com.ramonguimaraes.horacerta.utils.extensions.onlyDate
import kotlinx.coroutines.launch
import java.util.Calendar

class ScheduleViewModel(
    private val repository: ScheduleRepository,
    private val cashRepository: CashFlowRepository,
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

    fun confirmClient(appointment: Appointment, paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            cashRepository.save(
                CashRegister(
                    companyUid = companyUid.toString(),
                    total = appointment.services.sumOf { it.price },
                    clientName = appointment.clientName,
                    date = Calendar.getInstance(),
                    paymentMethod = PaymentMethod.CARD,
                    services = appointment.services
                )
            )
        }
    }

    fun cancel(appointment: Appointment) {
        viewModelScope.launch {
            val result = repository.delete(appointment.id, appointment.scheduledTimes)
            result.mapResourceSuccess {
                if (it) {
                    selectedDate.value?.let { load(it) }
                }
            }
        }
    }
}
