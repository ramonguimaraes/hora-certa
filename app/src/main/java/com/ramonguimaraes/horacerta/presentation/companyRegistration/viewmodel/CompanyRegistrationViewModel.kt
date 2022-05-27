package com.ramonguimaraes.horacerta.presentation.companyRegistration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.base.Result
import com.ramonguimaraes.horacerta.domain.companyRegistration.interactor.CompanyRegisterUseCase
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CompanyRegistrationViewModel(private val useCase: CompanyRegisterUseCase) : ViewModel() {

    var companyView: Company? = null

    private val mResult = MutableLiveData<Result<String>>()
    val result: LiveData<Result<String>>
        get() = mResult

    fun saveCompanyData() {
        mResult.value = Result.Loading
        viewModelScope.launch {
            useCase.invoke(companyView!!).collect {
                mResult.value = it
            }
        }
    }
}
