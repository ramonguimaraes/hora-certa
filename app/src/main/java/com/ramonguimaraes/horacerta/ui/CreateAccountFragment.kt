package com.ramonguimaraes.horacerta.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentCreateAccountBinding
import com.ramonguimaraes.horacerta.presenter.CreateAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAccountFragment : Fragment() {

    private val viewModel: CreateAccountViewModel by viewModel()
    private val binding: FragmentCreateAccountBinding by lazy {
        FragmentCreateAccountBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.btnCreateAccount.setOnClickListener {
            if (validate()) {
                viewModel.singUp(
                    binding.txtName.text.toString(),
                    binding.txtEmail.text.toString(),
                    binding.txtPassword.text.toString()
                )
            }
        }
        return binding.root
    }

    private fun validate(): Boolean {
        var isValid = true
        val name = binding.txtName.text.toString()
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val repeatedPassword = binding.txtRepeatPassword.text.toString()

        if (name.isBlank()) {
            isValid = false
        }

        if (email.isBlank()) {
            isValid = false
        }

        if (password.isBlank()) {
            isValid = false
        }

        if (repeatedPassword.isBlank()) {
            isValid = false
        }

        if (password != repeatedPassword) {
            isValid = false
        }

        return isValid
    }
}
