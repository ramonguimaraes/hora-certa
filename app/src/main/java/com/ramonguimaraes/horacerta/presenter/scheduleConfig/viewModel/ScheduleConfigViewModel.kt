package com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import com.ramonguimaraes.horacerta.domain.scheduleConfig.useCase.SaveScheduleConfigUseCase
import com.ramonguimaraes.horacerta.utils.DayOfWeek
import kotlinx.coroutines.launch
import java.time.LocalTime

class ScheduleConfigViewModel(
    private val saveScheduleConfigUseCase: SaveScheduleConfigUseCase,
    private val userUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val mSaveResponse = MutableLiveData<Resource<ScheduleConfig>>()
    val saveResponse get() = mSaveResponse

    private val mValidate = MutableLiveData<Boolean>()
    val validate get() = mValidate

    private val mOpenHour = MutableLiveData(LocalTime.of(8, 0))
    val openHour: LiveData<LocalTime> get() = mOpenHour

    private val mIntervalStartHour = MutableLiveData(LocalTime.of(12, 0))
    val intervalStartHour: LiveData<LocalTime> get() = mIntervalStartHour

    private val mIntervalEndHour = MutableLiveData(LocalTime.of(13, 0))
    val intervalEndHour: LiveData<LocalTime> get() = mIntervalEndHour

    private val mCloseHour = MutableLiveData(LocalTime.of(18, 0))
    val closeHour: LiveData<LocalTime> get() = mCloseHour

    var daysOfWeek = listOf<DayOfWeek>()

    private val dayOfWeek = MutableLiveData(DayOfWeek.SUNDAY)

    fun save() {
        if (validateFields()) {
            viewModelScope.launch {
                val result = saveScheduleConfigUseCase(getScheduleConfig())
                mSaveResponse.postValue(result)
            }
        }
    }

    private fun getScheduleConfig(): ScheduleConfig {
        return ScheduleConfig(
            companyUid = userUseCase.currentUid()!!,
            dayOfWeek = dayOfWeek.value!!,
            openTime = mOpenHour.value!!,
            intervalStart = mIntervalStartHour.value!!,
            intervalEnd = mIntervalEndHour.value!!,
            closeTime = mCloseHour.value!!
        )
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

    private fun validateOpenHour(): Boolean {
        val openHour = mOpenHour.value
        val intervalStartHour = mIntervalStartHour.value
        return openHour != null && intervalStartHour != null && openHour < intervalStartHour
    }

    private fun validateCloseHour(): Boolean {
        val closeHour = mCloseHour.value
        return closeHour != null
    }

    private fun validateIntervalStartHour(): Boolean {
        val intervalStartHour = mIntervalStartHour.value
        val intervalEnd = mIntervalEndHour.value
        return intervalStartHour != null && intervalEnd != null && intervalStartHour < intervalEnd
    }

    private fun validateIntervalEndHour(): Boolean {
        val intervalEnd = mIntervalEndHour.value
        val closeHour = mCloseHour.value
        return intervalEnd != null && closeHour != null && intervalEnd < closeHour
    }

    private fun validateDayOfWeek(): Boolean {
        val dayOfWeek = dayOfWeek.value
        return dayOfWeek != null && daysOfWeek.none { it.day == dayOfWeek.day }
    }

    fun setOpenHour(localTime: LocalTime?) {
        mOpenHour.postValue(localTime)
    }

    fun setCloseHour(localTime: LocalTime?) {
        mCloseHour.value = localTime
    }

    fun setIntervalStartHour(localTime: LocalTime?) {
        mIntervalStartHour.value = localTime
    }

    fun setIntervalEndHour(localTime: LocalTime?) {
        mIntervalEndHour.value = localTime
    }

    fun setDayOfWeek(dayOfWeek: DayOfWeek?) {
        this.dayOfWeek.value = dayOfWeek
    }
}
