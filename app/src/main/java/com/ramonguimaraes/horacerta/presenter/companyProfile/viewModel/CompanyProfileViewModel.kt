package com.ramonguimaraes.horacerta.presenter.companyProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.CompanyProfileValidationUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.SaveCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companyProfile.mapper.CompanyProfileViewMapper
import com.ramonguimaraes.horacerta.presenter.companyProfile.model.CompanyProfileView
import kotlinx.coroutines.launch

class CompanyProfileViewModel(
    private val saveCompanyProfileUseCase: SaveCompanyProfileUseCase,
    private val loadCompanyProfileUseCase: LoadCompanyProfileUseCase,
    private val companyProfileValidationUseCase: CompanyProfileValidationUseCase,
    private val companyProfileViewMapper: CompanyProfileViewMapper
) : ViewModel() {

    val segments = listOf("Esporte", "Saúde", "Educação", "Beleza")
    private val mSaveResult = MutableLiveData<Resource<Boolean>>()
    val saveResult get() = mSaveResult

    private val mProfileViewState = MutableLiveData<Resource<CompanyProfile?>>()
    val profileViewState: LiveData<Resource<CompanyProfile?>> get() = mProfileViewState


    val profileView = MutableLiveData(CompanyProfileView())


    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        mProfileViewState.value = Resource.Loading
        viewModelScope.launch {
            val result = loadCompanyProfileUseCase()
            mProfileViewState.postValue(result)
            result.mapResourceSuccess { model ->
                model?.let {
                    val view = companyProfileViewMapper.mapToView(it)
                    profileView.postValue(view)
                }
            }
        }
    }

    fun setSegment(selectedItem: String?) {
        if (selectedItem != null) {
            profileView.value?.companySegment = selectedItem
        }
    }

    fun setProfilePicUri(uri: Uri?) {
        if (uri != null) {
            val newProfile = profileView.value
            newProfile?.photoUri = uri
            profileView.value = newProfile
        }
    }

    fun saveAction() {
        if (formValidation()) {
            profileView.value?.let { companyProfileView ->
                val mapped = companyProfileViewMapper.mapFromView(companyProfileView)
                viewModelScope.launch {
                    val result = saveCompanyProfileUseCase(mapped)
                    mSaveResult.postValue(result)
                }
            }
        }
    }

    private fun formValidation(): Boolean {
        val cnpjResult = companyProfileValidationUseCase.cnpjValidation(profileView.value?.cnpj)
        val nameResult = companyProfileValidationUseCase.nameValidation(
            profileView.value?.companyName
        )
        val phoneResult = companyProfileValidationUseCase.phoneValidation(
            profileView.value?.phoneNumber
        )
        val segmentResult = companyProfileValidationUseCase.segmentValidation(
            profileView.value?.companySegment
        )

        profileView.value = profileView.value?.copy(
            companyNameError = nameResult.errorMessage,
            phoneNumberError = phoneResult.errorMessage,
            cnpjError = cnpjResult.errorMessage,
            companySegmentError = segmentResult.errorMessage
        )

        return listOf(
            nameResult,
            phoneResult,
            cnpjResult,
            segmentResult
        ).all { it.successFul }
    }
}
