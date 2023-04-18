package com.ramonguimaraes.horacerta.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramonguimaraes.horacerta.domain.authentication.useCase.SingUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateAccountViewModel(private val singUpUseCase: SingUpUseCase) : ViewModel() {

    fun singUp(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            singUpUseCase.execute(name, email, password)
        }
    }
}
