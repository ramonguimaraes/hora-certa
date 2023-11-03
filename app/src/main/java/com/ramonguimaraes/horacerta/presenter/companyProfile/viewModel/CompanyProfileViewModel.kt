package com.ramonguimaraes.horacerta.presenter.companyProfile.viewModel

import android.location.Address
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.AddressValidationUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.CompanyProfileValidationUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.SaveCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companyProfile.mapper.CompanyProfileViewMapper
import com.ramonguimaraes.horacerta.presenter.companyProfile.model.CompanyProfileView
import com.ramonguimaraes.horacerta.utils.AddressUtil
import kotlinx.coroutines.launch

class CompanyProfileViewModel(
    private val saveCompanyProfileUseCase: SaveCompanyProfileUseCase,
    private val loadCompanyProfileUseCase: LoadCompanyProfileUseCase,
    private val companyProfileValidationUseCase: CompanyProfileValidationUseCase,
    private val companyProfileViewMapper: CompanyProfileViewMapper,
    private val addressValidationUseCase: AddressValidationUseCase
) : ViewModel() {

    val segments = listOf("Esporte", "Saúde", "Educação", "Beleza")
    private val mSaveResult = MutableLiveData<Resource<Boolean>>()
    val saveResult get() = mSaveResult

    private val mProfileViewState = MutableLiveData<Resource<CompanyProfile?>>()
    val profileViewState: LiveData<Resource<CompanyProfile?>> get() = mProfileViewState

    val profileView = MutableLiveData(CompanyProfileView())
    val dismissDialog = MutableLiveData(false)

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

    private fun addressFormValidation(): Boolean {
        return profileView.value?.let { profile ->
            val streetNameResult = addressValidationUseCase.validateStreet(profile.rua)

            val numberResult = addressValidationUseCase.validateNumber(
                profile.numero,
                profile.semNumero
            )

            val neighborhoodResult = addressValidationUseCase.validateNeighborhood(profile.bairro)

            val cityResult = addressValidationUseCase.validateCity(profile.cidade)

            profileView.value = profileView.value?.copy(
                ruaErro = streetNameResult.errorMessage,
                numeroErro = numberResult.errorMessage,
                bairroErro = neighborhoodResult.errorMessage,
                cidadeErro = cityResult.errorMessage
            )

            listOf(
                streetNameResult,
                numberResult,
                neighborhoodResult,
                cityResult
            ).all { it.successFul }
        } ?: false
    }

    fun saveAddress() {
        if (addressFormValidation()) {
            profileView.value = profileView.value
            dismissDialog.postValue(true)
        }
    }

    fun setAddress(address: Address) {
        profileView.value = profileView.value?.copy(
            rua = address.thoroughfare ?: "",
            numero = address.subThoroughfare ?: "",
            cidade = address.subAdminArea ?: "",
            uf = AddressUtil.getStateAbbreviation(address) ?: "",
            latitude = address.latitude,
            longitude = address.longitude
        )
    }
}
