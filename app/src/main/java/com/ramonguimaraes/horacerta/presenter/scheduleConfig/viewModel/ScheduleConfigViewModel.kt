package com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase.SaveScheduleConfigUseCase
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import kotlinx.coroutines.launch
import java.time.LocalTime

class ScheduleConfigViewModel(
    private val saveScheduleConfigUseCase: SaveScheduleConfigUseCase
) : ViewModel() {

    private val mSaveResponse = MutableLiveData<Resource<ScheduleConfig>?>()
    val saveResponse get() = mSaveResponse

    private val mValidate = MutableLiveData<Boolean>()
    val validate get() = mValidate

    private val openHour = MutableLiveData<LocalTime?>()
    private val closeHour = MutableLiveData<LocalTime?>()
    private val intervalStartHour = MutableLiveData<LocalTime?>()
    private val intervalEndHour = MutableLiveData<LocalTime?>()
    private val dayOfWeek = MutableLiveData<DayOfWeek?>()

    fun save() {
        if (validateFields()) {
            viewModelScope.launch {
                mSaveResponse.postValue(
                    saveScheduleConfigUseCase(
                        ScheduleConfig(
                            companyUid = "",
                            dayOfWeek = dayOfWeek.value!!,
                            openTime = openHour.value!!,
                            intervalStart = intervalStartHour.value!!,
                            intervalEnd = intervalEndHour.value!!,
                            closeTime = closeHour.value!!
                        )
                    )
                )
            }
        }
    }

    private fun validateFields(): Boolean {
        val isValid = validateOpenHour() &&
                validateCloseHour() &&
                validateIntervalEndHour() &&
                validateIntervalStartHour() &&
                validateDayOfWeek()

        mValidate.postValue(isValid)

        return isValid
    }

    fun validateOpenHour(): Boolean {
        val openHour = openHour.value
        val intervalStartHour = intervalStartHour.value
        return openHour != null && intervalStartHour != null && openHour < intervalStartHour
    }

    fun validateCloseHour(): Boolean {
        val closeHour = closeHour.value
        return closeHour != null
    }

    fun validateIntervalStartHour(): Boolean {
        val intervalStartHour = intervalStartHour.value
        val intervalEnd = intervalEndHour.value
        return intervalStartHour != null && intervalEnd != null && intervalStartHour < intervalEnd
    }

    fun validateIntervalEndHour(): Boolean {
        val intervalEnd = intervalEndHour.value
        val closeHour = closeHour.value
        return intervalEnd != null && closeHour != null && intervalEnd < closeHour
    }

    fun validateDayOfWeek(): Boolean {
        val dayOfWeek = dayOfWeek.value
        return dayOfWeek != null
    }

    fun setOpenHour(localTime: LocalTime?) {
        openHour.postValue(localTime)
    }

    fun setCloseHour(localTime: LocalTime?) {
        closeHour.value = localTime
    }

    fun setIntervalStartHour(localTime: LocalTime?) {
        intervalStartHour.value = localTime
    }

    fun setIntervalEndHour(localTime: LocalTime?) {
        intervalEndHour.value = localTime
    }

    fun setDayOfWeek(dayOfWeek: DayOfWeek?) {
        this.dayOfWeek.value = dayOfWeek
    }
}
