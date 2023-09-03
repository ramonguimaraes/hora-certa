package com.ramonguimaraes.horacerta.presenter.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.ServiceItem
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.domain.schedule.repository.ScheduleRepository
import com.ramonguimaraes.horacerta.domain.schedule.useCase.GetAvailableHorsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class ScheduleRegistrationViewModel(
    private val useCase: GetAvailableHorsUseCase,
    private val repository: ScheduleRepository
) : ViewModel() {

    private val mLivedata = MutableLiveData<List<TimeInterval>>()
    val liveData get() = mLivedata

    private val mDate = MutableLiveData<Calendar>()
    val date: LiveData<Calendar> get() = mDate

    fun load(calendar: Calendar = Calendar.getInstance()) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val res = useCase.getAvailableHors(calendar)
                mLivedata.postValue(res)
            }
        }
    }

    private fun configuraListaDeHoras(checkedServices: MutableList<ServiceItem>) {
        val hours = liveData.value ?: emptyList()
        val total = checkedServices.sumOf { it.duration }
        val disponiveis: MutableList<TimeInterval> = mutableListOf()
        val qtdIntervalos = total / 30

        for (i in 0 until hours.size - (qtdIntervalos - 1)) {
            val subLista = hours.subList(i, i + qtdIntervalos)
            if (subLista.all { it.disponivel }) {
                disponiveis.add(hours[i])
            }
        }

        mLivedata.value = disponiveis
    }

    fun getToday(): Calendar {
        return Calendar.getInstance()
    }

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        mDate.value = calendar
    }

    fun setDate() {
        mDate.value = getToday()
    }

    fun save(it: TimeInterval) {
        val calendar = date.value
        calendar?.set(Calendar.HOUR_OF_DAY, it.time.hour)
        calendar?.set(Calendar.MINUTE, it.time.minute)
        calendar?.set(Calendar.SECOND, 0)

        val scheduleTime = ScheduledTime("", calendar!!, "", "")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.save(scheduleTime)
            }
        }
    }
}
