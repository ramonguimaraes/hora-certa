package com.ramonguimaraes.horacerta.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ramonguimaraes.horacerta.databinding.FragmentLoginBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.authentication.LoginViewModel
import com.ramonguimaraes.horacerta.utils.gone
import com.ramonguimaraes.horacerta.utils.hideKeyboard
import com.ramonguimaraes.horacerta.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()
    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.btnLogin.setOnClickListener {
            it.hideKeyboard()
            viewModel.login()
        }
        setObserver()
        return binding.root
    }

    private fun setObserver() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    hideLoading()
                    showSuccess()
                    moveToHome()
                }
                is Resource.Failure -> {
                    hideLoading()
                    showError()
                }
                is Resource.Loading -> showLoading()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visible()
    }

    private fun hideLoading() {
        binding.progressBar.gone()
    }
    private fun moveToHome() {}
    private fun showSuccess() {
        Toast.makeText(
            context,
            "Login com sucesso",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun showError() {
        Toast.makeText(context, "Login falhou", Toast.LENGTH_SHORT)
            .show()
    }
}
