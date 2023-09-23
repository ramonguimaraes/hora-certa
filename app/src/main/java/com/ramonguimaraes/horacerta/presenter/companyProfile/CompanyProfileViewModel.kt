package com.ramonguimaraes.horacerta.presenter.companyProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.SaveCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.scheduleConfig.model.ScheduleConfig
import kotlinx.coroutines.launch

class CompanyProfileViewModel(
    private val saveCompanyProfileUseCase: SaveCompanyProfileUseCase,
    private val loadCompanyProfileUseCase: LoadCompanyProfileUseCase
): ViewModel() {

    val segments = listOf("Esporte", "Saúde", "Educação", "Beleza")
    val profile = MutableLiveData(CompanyProfile())

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            val response = loadCompanyProfileUseCase()
            response.mapResourceSuccess {
                profile.postValue(it ?: CompanyProfile())
            }
        }
    }

    fun save() {
        viewModelScope.launch {

            if (profile.value != null) {
                saveCompanyProfileUseCase(profile.value!!)
            }
        }
    }

    fun setSegment(selectedItem: String?) {
        if (selectedItem != null) {
            profile.value?.companySegment= selectedItem
        }
    }
}
