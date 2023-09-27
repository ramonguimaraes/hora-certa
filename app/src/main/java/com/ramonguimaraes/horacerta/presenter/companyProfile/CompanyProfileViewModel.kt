package com.ramonguimaraes.horacerta.presenter.companyProfile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.SaveCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import kotlinx.coroutines.launch

class CompanyProfileViewModel(
    private val saveCompanyProfileUseCase: SaveCompanyProfileUseCase,
    private val loadCompanyProfileUseCase: LoadCompanyProfileUseCase
): ViewModel() {

    val segments = listOf("Esporte", "Saúde", "Educação", "Beleza")
    val profile = MutableLiveData(CompanyProfile())
    private val mSaveResult = MutableLiveData<Resource<Boolean>>()
    val saveResult get() = mSaveResult

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
                val result = saveCompanyProfileUseCase(profile.value!!)
                mSaveResult.postValue(result)
            }
        }
    }

    fun setSegment(selectedItem: String?) {
        if (selectedItem != null) {
            profile.value?.companySegment= selectedItem
        }
    }

    fun setProfilePicUri(uri: Uri?) {
        if (uri != null) {
            val newProfile = profile.value
            newProfile?.photoUri = uri
            profile.value = newProfile
        }
    }
}
