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

    private val mOpenHour = MutableLiveData<LocalTime?>()
    private val mCloseHour = MutableLiveData<LocalTime?>()
    private val mIntervalStartHour = MutableLiveData<LocalTime?>()
    private val mIntervalEndHour = MutableLiveData<LocalTime?>()
    private val mDayOfWeek = MutableLiveData<DayOfWeek?>()

    fun save() {
        viewModelScope.launch {
            saveScheduleConfigUseCase(ScheduleConfig(
                companyUid = "",
                dayOfWeek = mDayOfWeek.value!!,
                openTime = mOpenHour.value!!,
                intervalStart = mIntervalStartHour.value!!,
                intervalEnd = mIntervalEndHour.value!!,
                closeTime = mCloseHour.value!!
            ))
        }
    }

    fun setOpenHour(localTime: LocalTime?) {
        mOpenHour.value = localTime
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
        mDayOfWeek.value = dayOfWeek
    }
}
