package com.ramonguimaraes.horacerta.presenter.splashScreen.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.presenter.splashScreen.viewModel.SplashScreenViewModel
import com.ramonguimaraes.horacerta.domain.user.model.AccountType
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.directions.observe(viewLifecycleOwner) { direction ->
            when (direction) {
                SplashScreenViewModel.Directions.HOME_COMPANY -> goToCompanyHome()
                SplashScreenViewModel.Directions.HOME_CLIENT -> goToClientHome()
                SplashScreenViewModel.Directions.COMPANY_PROFILE -> goToCompanyProfile()
                else -> goToLogin()
            }
        }

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    private fun goToClientHome() {
        val action =
            SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment(AccountType.CLIENT)
        findNavController().navigate(action)
    }

    private fun goToCompanyHome() {
        val action =
            SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment(AccountType.COMPANY)
        findNavController().navigate(action)
    }

    private fun goToCompanyProfile() {
        findNavController().navigate(R.id.action_splashScreenFragment_to_companyProfileFragment)
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
    }
}
