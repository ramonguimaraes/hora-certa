package com.ramonguimaraes.horacerta.ui.companyRegistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentRegisterBinding
import com.ramonguimaraes.horacerta.domain.companyRegistration.model.Company
import com.ramonguimaraes.horacerta.presentation.companyRegistration.viewmodel.CompanyRegistrationViewModel
import com.ramonguimaraes.horacerta.presentation.companyRegistration.model.CompanyView
import com.ramonguimaraes.horacerta.ui.validate
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {

    private lateinit var mBinding: FragmentRegisterBinding
    private val mViwModel: CompanyRegistrationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRegisterBinding.inflate(inflater)

        configButtonRegister()
        return mBinding.root
    }

    private fun configButtonRegister() {
        mBinding.buttonRegisterNext.setOnClickListener {
            val email = mBinding.editTextTextEmail.text.toString()
            val password = mBinding.editTextTextPassword.text.toString()
            val companyName = mBinding.editTextTextCompanyName.text.toString()

            if (validateFields()) {
                createAccount(email, password, companyName)
            }
        }
    }

    private fun createAccount(email: String, password: String, companyName: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "empresa cadastrada", Toast.LENGTH_SHORT).show()

                    mViwModel.companyView = Company(
                        it.result.user!!.uid,
                        companyName,
                        email
                    )

                    findNavController().navigate(R.id.action_registerFragment_to_addressFragment)
                } else {
                    // TODO TRATAR OS ERROS POSSIVEIS
                    Toast.makeText(context, "deu erro", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Valida os campos do formulario de cadastro
     *
     * @return isValid: Boolean
     */
    private fun validateFields(): Boolean {
        var isValid = true

        val password = mBinding.editTextTextPassword.text.toString()
        val passwordRepeat = mBinding.editTextTextRepeatPassword.text.toString()

        isValid = mBinding.editTextTextCompanyName.validate(isValid = isValid)
        isValid = mBinding.editTextTextEmail.validate(isValid = isValid)
        isValid = mBinding.editTextTextPassword.validate(isValid = isValid)
        isValid = mBinding.editTextTextRepeatPassword.validate(isValid = isValid)

        if (password != passwordRepeat) {
            isValid = false
        }

        return isValid
    }
}
