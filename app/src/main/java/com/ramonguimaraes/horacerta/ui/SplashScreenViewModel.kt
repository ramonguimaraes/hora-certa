package com.ramonguimaraes.horacerta.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.repository.AuthRepository
import com.ramonguimaraes.horacerta.domain.authentication.useCase.GetCurrentUserUseCase
import com.ramonguimaraes.horacerta.domain.companyProfile.useCase.LoadCompanyProfileUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.utils.AccountType
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val authRepository: AuthRepository,
    private val userUseCase: GetCurrentUserUseCase,
    private val loadCompanyProfileUseCase: LoadCompanyProfileUseCase
) : ViewModel() {

    // Verificar se usuario está logado, se usuarios estiver logado, verificar o tipo de usuario
    // se for tipo Company, verificar se existe perfil criado

    enum class Directions {
        LOGIN,
        COMPANY_PROFILE,
        HOME_CLIENT,
        HOME_COMPANY
    }

    private val mDirections = MutableLiveData<Directions>()
    val directions: LiveData<Directions> get() = mDirections

    init {
        viewModelScope.launch {
            if (isLogged()) {
                val userType = userType()
                if (userType == AccountType.CLIENT) {
                    mDirections.postValue(Directions.HOME_CLIENT)
                } else if (userType == AccountType.COMPANY) {
                    if (getProfile()) {
                        mDirections.postValue(Directions.HOME_COMPANY)
                    } else {
                        mDirections.postValue(Directions.COMPANY_PROFILE)
                    }
                } else {
                    mDirections.postValue(Directions.LOGIN)
                }
            } else {
                mDirections.postValue(Directions.LOGIN)
            }
        }
    }

    private fun isLogged(): Boolean {
        return authRepository.currentUser != null
    }

    private suspend fun userType(): AccountType? {
        val userResource = userUseCase()
        return if (userResource is Resource.Success) {
            userResource.result?.accountType
        } else {
            AccountType.NONE
        }
    }

    private suspend fun getProfile(): Boolean {
        val profileResource = loadCompanyProfileUseCase()
        return if (profileResource is Resource.Success) {
            profileResource.result?.id != ""
        } else {
            false
        }
    }
}