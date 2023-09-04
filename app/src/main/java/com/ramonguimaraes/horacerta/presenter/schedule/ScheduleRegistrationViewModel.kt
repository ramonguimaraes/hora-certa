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
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.util.Calendar

class ScheduleRegistrationViewModel(
    private val useCase: GetAvailableHorsUseCase,
    private val repository: ScheduleRepository
) : ViewModel() {

    private val mLivedata = MutableLiveData<List<TimeInterval>>()
    val liveData get() = mLivedata

    private val mServices = MutableLiveData<List<ServiceItem>>()
    val services get() = mServices

    private val mDate = MutableLiveData<Calendar>()
    val date: LiveData<Calendar> get() = mDate

    private val mTotalTime = MutableLiveData(0)
    val totalTime get() = mTotalTime

    init {
        mServices.value = listOf(
            ServiceItem("Corte de cabelo", false, 60),
            ServiceItem("Barba", false, 90),
            ServiceItem("Pintura", false, 30),
            ServiceItem("Corte personalizado", false, 30),
            ServiceItem("Sombrancelha", false, 200)
        )
    }

    fun load(calendar: Calendar = Calendar.getInstance()) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val res = useCase.getAvailableHors(calendar)
                mLivedata.postValue(res)
                // Dando update no totalTime com o valor dele mesmo
                // garanto que o usuario não tenha que clicar de novo
                // nos servicos mesmo que ele mude de data
                // TODO: 23/09/2023 Aplicar melhoria nesse código
                mTotalTime.postValue(totalTime.value)
            }
        }
    }

    fun configuraListaDeHoras(totalTime: Int) {
        val hours = liveData.value ?: emptyList()
        val qtdIntervalos = totalTime / 30

        if (qtdIntervalos > 0) {
            for (i in hours.indices) {
                val subList = hours.subListOrNull(i, i + qtdIntervalos)
                if (subList != null) {
                    hours[i].show = subList.all { it.disponivel }
                } else {
                    hours[i].show = false
                }
            }
        } else {
            hours.forEach {
                it.show = it.disponivel
            }
        }

        mLivedata.value = hours
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

    fun updateTotalTime(item: ServiceItem, isChecked: Boolean) {
        if (isChecked) {
            mTotalTime.value = mTotalTime.value?.plus(item.duration)
        } else {
            mTotalTime.value = mTotalTime.value?.minus(item.duration)
        }
    }
}

fun <E> List<E>.subListOrNull(startOf: Int, endOf: Int): List<E>? {
    return try {
        subList(startOf, endOf)
    } catch (e: Exception) {
        null
    }
}
