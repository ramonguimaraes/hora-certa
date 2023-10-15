package com.ramonguimaraes.horacerta.presenter.companiesList.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.companiesList.useCase.LoadCompaniesUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource
import kotlinx.coroutines.launch

class CompaniesViewModel(
    private val loadCompaniesUseCase: LoadCompaniesUseCase
) : ViewModel() {

    private val mCompanies = MutableLiveData<Resource<List<CompanyProfile>>>()
    val companies: LiveData<Resource<List<CompanyProfile>>> get() = mCompanies

    init {
        viewModelScope.launch {
            mCompanies.postValue(Resource.Loading)
            val companiesResource = loadCompaniesUseCase.execute()
            mCompanies.postValue(companiesResource)
        }
    }
}
