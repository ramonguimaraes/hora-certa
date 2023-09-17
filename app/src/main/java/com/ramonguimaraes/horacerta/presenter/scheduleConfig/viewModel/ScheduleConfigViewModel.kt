package com.ramonguimaraes.horacerta.presenter.scheduleConfig.viewModel

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
    userUseCase: GetCurrentUserUseCase
) : ViewModel() {

    val scheduleConfig = MutableLiveData<ScheduleConfig?>()

    private val mSaveResponse = MutableLiveData<Resource<ScheduleConfig>?>()
    val saveResponse get() = mSaveResponse

    private val mValidate = MutableLiveData<Boolean>()
    val validate get() = mValidate

    var daysOfWeek = listOf<DayOfWeek>()
    var isUpdating = false

    private val dayOfWeek = MutableLiveData(DayOfWeek.SUNDAY)

    init {
        scheduleConfig.value = ScheduleConfig(
            companyUid = userUseCase.currentUid() ?: "",
            dayOfWeek = DayOfWeek.SUNDAY,
            openTime = LocalTime.of(8, 0),
            intervalStart = LocalTime.of(12, 0),
            intervalEnd = LocalTime.of(13, 0),
            closeTime = LocalTime.of(17, 0),
        )
    }

    fun save() {
        if (validateFields()) {
            viewModelScope.launch {
                val result = scheduleConfig.value?.let {
                    saveScheduleConfigUseCase(it)
                }
                mSaveResponse.postValue(result)
            }
        }
    }

    private fun validateFields(): Boolean {
        val isValid = validateOpenTime() &&
                validateCloseTime() &&
                validateIntervalEnd() &&
                validateIntervalStart() &&
                (validateDayOfWeek() or isUpdating)


        mValidate.postValue(isValid)

        return isValid
    }

    private fun validateOpenTime(): Boolean {
        val openTime = scheduleConfig.value?.openTime
        val intervalStart = scheduleConfig.value?.intervalStart
        return openTime != null && intervalStart != null && openTime < intervalStart
    }

    private fun validateCloseTime(): Boolean {
        val closeTime = scheduleConfig.value?.closeTime
        return closeTime != null
    }

    private fun validateIntervalStart(): Boolean {
        val intervalStart = scheduleConfig.value?.intervalStart
        val intervalEnd = scheduleConfig.value?.intervalEnd
        return intervalStart != null && intervalEnd != null && intervalStart < intervalEnd
    }

    private fun validateIntervalEnd(): Boolean {
        val intervalEnd = scheduleConfig.value?.intervalEnd
        val closeTime = scheduleConfig.value?.closeTime
        return intervalEnd != null && closeTime != null && intervalEnd < closeTime
    }

    private fun validateDayOfWeek(): Boolean {
        val dayOfWeek = dayOfWeek.value
        return dayOfWeek != null && daysOfWeek.none { it.day == dayOfWeek.day }
    }

    fun setOpenTime(localTime: LocalTime?) {
        if (localTime != null) {
            val currentScheduleConfig = scheduleConfig.value
            currentScheduleConfig?.openTime = localTime
            scheduleConfig.value = currentScheduleConfig
        }
    }

    fun setCloseTime(localTime: LocalTime?) {
        if (localTime != null) {
            val currentScheduleConfig = scheduleConfig.value
            currentScheduleConfig?.closeTime = localTime
            scheduleConfig.value = currentScheduleConfig
        }
    }

    fun setIntervalStart(localTime: LocalTime?) {
        if (localTime != null) {
            val currentScheduleConfig = scheduleConfig.value
            currentScheduleConfig?.intervalStart = localTime
            scheduleConfig.value = currentScheduleConfig
        }
    }

    fun setIntervalEnd(localTime: LocalTime?) {
        if (localTime != null) {
            val currentScheduleConfig = scheduleConfig.value
            currentScheduleConfig?.intervalEnd = localTime
            scheduleConfig.value = currentScheduleConfig
        }
    }

    fun setDayOfWeek(dayOfWeek: DayOfWeek?) {
        if (dayOfWeek != null) {
            scheduleConfig.value?.dayOfWeek = dayOfWeek
        }
    }
}
