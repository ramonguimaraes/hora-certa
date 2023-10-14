package com.ramonguimaraes.horacerta.presenter.authentication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentCreateAccountBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.authentication.viewModel.CreateAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAccountFragment : Fragment() {

    private val viewModel: CreateAccountViewModel by viewModel()
    private val binding: FragmentCreateAccountBinding by lazy {
        FragmentCreateAccountBinding.inflate(layoutInflater)
    }
    private val args: CreateAccountFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.accountType = args.accountType
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.accountCreated.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    showSuccess()
                    moveToLogin()
                }
                is Resource.Failure -> {
                    hideLoading()
                    showError(it.exception.message.toString())
                }
            }
        }

        viewModel.singUpError.observe(viewLifecycleOwner) {
            if (it is Resource.Failure) {
                hideLoading()
                showError(it.exception.message.toString())
            }
        }
    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        Toast.makeText(context, "Criando conta", Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess() {
        Toast.makeText(
            context,
            "Conta criada, um link de verificacao foi enviado para o seu email",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun hideLoading() {}

    private fun moveToLogin() {
        findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
    }
}
