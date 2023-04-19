package com.ramonguimaraes.horacerta.presenter.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.ramonguimaraes.horacerta.domain.authentication.useCase.LoginUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.utils.isEmail
import com.ramonguimaraes.horacerta.utils.isValid
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    var password = MutableLiveData("")
    var passwordError = MutableLiveData<String>()
    var email = MutableLiveData("")
    var emailError = MutableLiveData<String>()

    private val mUser = MutableLiveData<Resource<FirebaseUser?>>()
    val user: LiveData<Resource<FirebaseUser?>> get() = mUser

    fun login() {
        if (isFormValid()) {
            mUser.value = Resource.Loading
            viewModelScope.launch {
                val result = loginUseCase.execute(email.value!!, password.value!!)
                mUser.postValue(result)
            }
        }
    }

    private fun isFormValid(): Boolean {
        return validatePassword(password.value) and validateEmail(email.value)
    }

    private fun validateEmail(email: String?): Boolean {
        return when {
            !email.isValid() -> {
                emailError.value = "Campo obrigatório"
                false
            }
            !email.isEmail() -> {
                emailError.value = "Email inválido"
                false
            }
            else -> {
                emailError.value = ""
                true
            }
        }
    }

    private fun validatePassword(password: String?): Boolean {
        return if (!password.isValid()) {
            passwordError.value = "Campo obrigatório"
            false
        } else {
            passwordError.value = ""
            true
        }
    }
}
