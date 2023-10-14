package com.ramonguimaraes.horacerta.presenter.authentication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.ramonguimaraes.horacerta.domain.authentication.useCase.SingUpUseCase
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.domain.user.model.User
import com.ramonguimaraes.horacerta.domain.user.useCase.SaveUserUseCase
import com.ramonguimaraes.horacerta.domain.user.model.AccountType
import com.ramonguimaraes.horacerta.utils.extensions.isEmail
import com.ramonguimaraes.horacerta.utils.extensions.isValid
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val singUpUseCase: SingUpUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    var accountType: AccountType = AccountType.CLIENT
    var name = MutableLiveData("")
    var nameError = MutableLiveData<String?>()
    var email = MutableLiveData("")
    var emailError = MutableLiveData<String?>()
    var password = MutableLiveData("")
    var passwordError = MutableLiveData<String?>()
    var repeatedPassword = MutableLiveData("")
    var repeatedPasswordError = MutableLiveData<String?>()
    private val mSingUpError = MutableLiveData<Resource<FirebaseUser?>>()
    val singUpError: LiveData<Resource<FirebaseUser?>> get() = mSingUpError

    private val mAccountCreated = MutableLiveData<Resource<DocumentReference?>>()
    val accountCreated: LiveData<Resource<DocumentReference?>> get() = mAccountCreated

    fun singUp() {
        if (validate()) {
            singUp(name.value!!, email.value!!, password.value!!, accountType)
        }
    }

    private fun singUp(name: String, email: String, password: String, accountType: AccountType) {
        viewModelScope.launch {
            val docRef = saveUser(name, email, accountType)
            if (docRef is Resource.Success) {
                val res = singUpUseCase.execute(email, password)
                if (res is Resource.Success) {
                    res.result?.uid?.let {
                        val user = User(
                            uid = it,
                            name = name,
                            email = email,
                            accountType = accountType
                        )
                        val accountCreated = saveUserUseCase.execute(docRef.result, user)
                        mAccountCreated.postValue(accountCreated)
                    }
                } else {
                    mSingUpError.postValue(res)
                }
            }
        }
    }

    private suspend fun saveUser(
        name: String,
        email: String,
        accountType: AccountType
    ): Resource<DocumentReference?> {
        val user = User(name = name, email = email, accountType = accountType)
        return saveUserUseCase.execute(user = user)
    }

    private fun validate(): Boolean {
        var isValid = true
        val name = name.value
        val email = email.value
        val password = password.value
        val repeatedPassword = repeatedPassword.value

        if (name.isValid()) {
            nameError.value = ""
        } else {
            nameError.value = "Campo obrigarótio"
            isValid = false
        }

        emailError.value = when {
            !email.isValid() -> "Campo obrigarótio"
            !email.isEmail() -> "Email invalido"
            else -> ""
        }

        if (password.isValid()) {
            passwordError.value = ""
        } else {
            passwordError.value = "Campo obrigarótio"
            isValid = false
        }

        repeatedPasswordError.value = when {
            !repeatedPassword.isValid() -> "Campo obrigatório"
            repeatedPassword != password -> "As senhas digitadas não coincidem"
            else -> ""
        }

        return isValid
    }
}
