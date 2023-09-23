package com.ramonguimaraes.horacerta.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.home.HomeViewModel
import com.ramonguimaraes.horacerta.ui.companyProfile.CompanyProfileFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var viewSwitcher: ViewSwitcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        viewSwitcher = root.findViewById(R.id.viewSwitcherHome)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.profileResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> showSuccess(it.result)
                is Resource.Loading -> showLoading()
                is Resource.Failure -> showError()
                else -> {}
            }
        }

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.homeMainContainer) as NavHostFragment?
        val navController = navHostFragment?.navController
        val navView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (navController != null) {
            navView.setupWithNavController(navController)
        }
    }

    private fun showSuccess(result: CompanyProfile?) {
        if (result != null) {
            viewSwitcher?.displayedChild = 1
            return
        }
        goToProfile()
    }

    private fun showError() {
        viewSwitcher?.displayedChild = 1
    }

    private fun showLoading() {
        viewSwitcher?.displayedChild = 0
    }

    private fun goToProfile() {
        val supportFragmentManager = activity?.supportFragmentManager
        supportFragmentManager?.beginTransaction()?.replace(
            R.id.fragmentContainerView,
            CompanyProfileFragment(),
            "companyProfileFragment"
        )?.addToBackStack(null)?.commit()
    }
}
