package com.ramonguimaraes.horacerta.presenter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.SingUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateAccountViewModel(private val singUpUseCase: SingUpUseCase) : ViewModel() {

    var name = MutableLiveData("")
    var nameError = MutableLiveData<String?>()
    var email = MutableLiveData("")
    var emailError = MutableLiveData<String?>()
    var password = MutableLiveData("")
    var passwordError = MutableLiveData<String?>()
    var repeatedPassword = MutableLiveData("")
    var repeatedPasswordError = MutableLiveData<String?>()

    fun singUp() {
        if (validate()) {
            singUp(name.value!!, email.value!!, password.value!!)
        }
    }

    private fun singUp(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            singUpUseCase.execute(name, email, password)
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        val name = name.value
        val email = email.value
        val password = password.value
        val repeatedPassword = repeatedPassword.value

        if (validateString(name)) {
            nameError.value = ""
        } else {
            nameError.value = "Campo obrigarótio"
            isValid = false
        }

        if (validateString(email)) {
            emailError.value = ""
        } else {
            emailError.value = "Campo obrigarótio"
            isValid = false
        }

        if (validateString(password)) {
            passwordError.value = ""
        } else {
            passwordError.value = "Campo obrigarótio"
            isValid = false
        }

        if (validateString(repeatedPassword)) {
            repeatedPasswordError.value = ""
        } else {
            repeatedPasswordError.value = "Campo obrigarótio"
            isValid = false
        }

        return isValid
    }

    private fun validateString(value: String?): Boolean {
        return value != null && value.isNotBlank() && value.isNotEmpty()
    }
}
