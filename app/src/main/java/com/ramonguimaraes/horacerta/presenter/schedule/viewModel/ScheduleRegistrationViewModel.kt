package com.ramonguimaraes.horacerta.presenter.schedule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.schedule.model.ScheduledTime
import com.ramonguimaraes.horacerta.domain.schedule.model.TimeInterval
import com.ramonguimaraes.horacerta.domain.schedule.useCase.GetAvailableHorsUseCase
import com.ramonguimaraes.horacerta.domain.schedule.useCase.DeleteScheduledTimeUseCase
import com.ramonguimaraes.horacerta.domain.schedule.useCase.RescheduleUseCase
import com.ramonguimaraes.horacerta.domain.schedule.useCase.SaveScheduledTimeUseCase
import com.ramonguimaraes.horacerta.domain.services.model.Service
import com.ramonguimaraes.horacerta.domain.services.userCase.LoadServicesUseCase
import com.ramonguimaraes.horacerta.domain.user.model.User
import com.ramonguimaraes.horacerta.presenter.scheduleClient.ClientAppointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class ScheduleRegistrationViewModel(
    private val getAvailableHorsUseCase: GetAvailableHorsUseCase,
    private val saveScheduledTimeUseCase: SaveScheduledTimeUseCase,
    private val deleteScheduledTimeUseCase: DeleteScheduledTimeUseCase,
    private val rescheduleUseCase: RescheduleUseCase,
    private val loadServiceUseCase: LoadServicesUseCase,
    private val userUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val mLivedata = MutableLiveData<List<TimeInterval>>()
    val liveData get() = mLivedata

    private val mServices = MutableLiveData<List<Service>>()
    val services get() = mServices

    private val mDate = MutableLiveData<Calendar>()
    val date: LiveData<Calendar> get() = mDate

    private val mTotalTime = MutableLiveData(0)
    val totalTime get() = mTotalTime

    private val mSaveResult = MutableLiveData<Resource<Boolean?>>()
    val saveResult: LiveData<Resource<Boolean?>> get() = mSaveResult

    private val currentUser = MutableLiveData<User?>()

    private var companyUid: String = ""

    init {
        viewModelScope.launch {
            userUseCase().mapResourceSuccess { currentUser.postValue(it) }
        }
    }

    fun loadServices() {
        viewModelScope.launch {
            val services = loadServiceUseCase(companyUid)
            services.mapResourceSuccess {
                mServices.postValue(it)
            }
        }
    }

    fun load(calendar: Calendar = Calendar.getInstance()) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val res = getAvailableHorsUseCase.getAvailableHors(calendar, companyUid)
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
        var qtdIntervalos = totalTime / 30
        if (totalTime > 0 && totalTime % 30 > 0) {
            qtdIntervalos += 1
        }
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

    fun save(timeInterval: TimeInterval) {
        mSaveResult.value = Resource.Loading
        val totalTime = totalTime.value
        val services = services.value
        val user = currentUser.value
        val calendar = date.value

        if (totalTime != null && services != null && user != null && calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, timeInterval.time.hour)
            calendar.set(Calendar.MINUTE, timeInterval.time.minute)
            calendar.set(Calendar.SECOND, 0)

            viewModelScope.launch {
                val result = saveScheduledTimeUseCase.save(
                    timeNeeded = totalTime,
                    services = services.filter { it.checked },
                    user = user,
                    calendar = calendar,
                    companyUid = companyUid
                )
                mSaveResult.postValue(result)
            }
        }
    }

    fun update(
        timeInterval: TimeInterval,
        appointmentId: String,
        scheduledTimes: List<ScheduledTime>
    ) {
        val calendar = date.value
        val totalTime = totalTime.value
        val services = services.value
        val user = currentUser.value

        if (totalTime != null && services != null && user != null && calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, timeInterval.time.hour)
            calendar.set(Calendar.MINUTE, timeInterval.time.minute)
            calendar.set(Calendar.SECOND, 0)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val result = rescheduleUseCase.execute(appointmentId, scheduledTimes, calendar, user.uid)
                    mSaveResult.postValue(result)
                }
            }
        }
    }

    fun reschedule(
        timeInterval: TimeInterval,
        appointmentId: String,
        scheduledTimes: List<ScheduledTime>
    ) {
        update(timeInterval, appointmentId, scheduledTimes)
    }

    fun updateTotalTime(item: Service, isChecked: Boolean) {
        if (isChecked) {
            mTotalTime.value = mTotalTime.value?.plus(item.estimatedDuration.toInt())
        } else {
            mTotalTime.value = mTotalTime.value?.minus(item.estimatedDuration.toInt())
        }
    }

    fun setCompanyUid(companyUid: String) {
        this.companyUid = companyUid
    }

    fun setCheckedServices(
        services: List<Service>,
        stringArrayList: List<String>?
    ): List<Service> {
        services.forEach {
            if (stringArrayList?.contains(it.id) == true) {
                it.checked = true
                updateTotalTime(it, true)
            }
        }
        return services
    }
}

fun <E> List<E>.subListOrNull(startOf: Int, endOf: Int): List<E>? {
    return try {
        subList(startOf, endOf)
    } catch (e: Exception) {
        null
    }
}
