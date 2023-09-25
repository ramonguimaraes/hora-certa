package com.ramonguimaraes.horacerta.presenter.home

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.authentication.useCase.LogoutUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.utils.AccountType
import kotlinx.coroutines.launch

class HomeViewModel(
    private val loadCompanyProfileUseCase: LoadCompanyProfileUseCase,
    private val userUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun logout() {
        logoutUseCase()
    }

    private val mProfileResult = MutableLiveData<Resource<CompanyProfile?>?>()
    val profileResult get() = mProfileResult

    init {
        viewModelScope.launch {
            if (userUseCase.getUserType().getResultData()?.accountType == AccountType.COMPANY) {
                mProfileResult.value = Resource.Loading
                mProfileResult.postValue(loadCompanyProfileUseCase())
            }
        }
    }
}
