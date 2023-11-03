package com.ramonguimaraes.horacerta.presenter.companiesList.viewModel

import android.location.Address
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.companiesList.useCase.LoadCompaniesUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.utils.AddressUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CompaniesViewModel(
    private val loadCompaniesUseCase: LoadCompaniesUseCase
) : ViewModel() {
    val segments = listOf("Todos", "Esporte", "Saúde", "Educação", "Beleza")
    private val mCompanies = MutableLiveData<Resource<List<CompanyProfile>>>()
    val companies: LiveData<Resource<List<CompanyProfile>>> get() = mCompanies
    private var loadJob: Job? = null

    fun load(address: Address?) {
        if (loadJob?.isActive == true) return
        val city = address?.subAdminArea ?: ""
        val uf = AddressUtil.getStateAbbreviation(address) ?: ""

        loadJob = viewModelScope.launch {
            mCompanies.postValue(Resource.Loading)
            val companies = loadCompaniesUseCase.execute(city, uf)
            mCompanies.postValue(companies)
            Log.d("CompaniesViewModel", "LOAD")
        }
    }

    fun loadBySegment(segment: String, address: Address?) {
        if (loadJob?.isActive == true) return
        if (segment == "Todos") {
            load(address)
            return
        }
        val city = address?.subAdminArea ?: ""
        val uf = AddressUtil.getStateAbbreviation(address) ?: ""

        loadJob = viewModelScope.launch {
            mCompanies.postValue(Resource.Loading)
            val companiesResource = loadCompaniesUseCase.execute(segment, city, uf)
            mCompanies.postValue(companiesResource)
            Log.d("CompaniesViewModel", "loadBySegment")
        }
    }
}
