package com.ramonguimaraes.horacerta.presenter.cashFlow.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.cashFlow.CashRegister
import com.ramonguimaraes.horacerta.domain.cashFlow.repository.CashFlowRepository
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.cashFlow.model.PaymentMethod
import com.ramonguimaraes.horacerta.utils.extensions.onlyDate
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date

class CashFlowViewModel(
    currentUserUseCase: GetCurrentUserUseCase,
    private val repository: CashFlowRepository
) : ViewModel() {
    val periods = listOf(Period.Today, Period.ThisWeek, Period.ThisMouth)
    private var currentUid = ""

    private val mCashRegisters = MutableLiveData<Resource<List<CashRegister>>>()
    val cashRegisters: LiveData<Resource<List<CashRegister>>> get() = mCashRegisters

    private val mPaymentMethod = MutableLiveData<String>("")
    val paymentMethod: LiveData<String> get() = mPaymentMethod

    private val mTotal = MutableLiveData(0.0)
    val total: LiveData<Double> get() = mTotal

    private val mAvarege = MutableLiveData(0.0)
    val average: LiveData<Double> get() = mAvarege

    init {
        currentUid = currentUserUseCase.currentUid().toString()
    }

    fun load(period: Period) {
        viewModelScope.launch {
            val result = repository.load(currentUid, getDateRange(period))
            mCashRegisters.postValue(result)
            result.mapResourceSuccess {
                mTotal.postValue(it.sumOf { it.total })
                mAvarege.postValue(it.sumOf { it.total } / it.size)
            }
        }
    }

    private fun getDateRange(period: Period): Pair<Date, Date> {
        val now = Calendar.getInstance().time

        return when (period) {
            is Period.Today -> {
                val initOfDay = Calendar.getInstance().onlyDate().time
                Pair(initOfDay, now)
            }

            is Period.ThisWeek -> {
                val localDate = LocalDate.now()
                val inicioDaSemana: LocalDate =
                    localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                val instant = inicioDaSemana.atStartOfDay(ZoneId.systemDefault()).toInstant()

                Pair(Date.from(instant), now)
            }

            is Period.ThisMouth -> {
                val local = LocalDate.now()
                val initOfMouth = local.withDayOfMonth(1)
                val instant = initOfMouth.atStartOfDay(ZoneId.systemDefault()).toInstant()
                Pair(Date.from(instant), now)
            }
        }
    }

    fun loadByPeriod(period: Period) {
        viewModelScope.launch {
            val result = repository.load(currentUid, getDateRange(period))
            mCashRegisters.postValue(result)
            result.mapResourceSuccess {
                mTotal.postValue(it.sumOf { it.total })
                mAvarege.postValue(it.sumOf { it.total } / it.size)
            }
        }
    }

    fun setPaymentMethodFilter(paymentMethod: PaymentMethod, isChecked: Boolean) {
        if (isChecked) {
            mPaymentMethod.value += paymentMethod.toString()
        } else {
            mPaymentMethod.value = mPaymentMethod.value?.replace(paymentMethod.toString(), "")
        }

        mCashRegisters.value?.mapResourceSuccess { list ->
            val filtered = list.filter { item ->
                if (mPaymentMethod.value.isNullOrEmpty()) {
                    return@filter true
                }
                mPaymentMethod.value.toString().contains(item.paymentMethod.toString())
            }
            mTotal.postValue(filtered.sumOf { item -> item.total })
            mAvarege.postValue(filtered.sumOf { item -> item.total } / filtered.size)
        }
    }
}

sealed class Period(private val description: String) {
    object Today : Period("Hoje")
    object ThisWeek : Period("Está semana")
    object ThisMouth : Period("Este mês")

    override fun toString(): String {
        return description
    }
}
