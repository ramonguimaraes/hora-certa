package com.ramonguimaraes.horacerta.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.domain.companyProfile.model.CompanyProfile
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companyProfile.model.CompanyProfileView
import com.ramonguimaraes.horacerta.presenter.home.HomeViewModel
import com.ramonguimaraes.horacerta.ui.companyProfile.CompanyProfileFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.homeMainContainer) as NavHostFragment?
        val navController = navHostFragment?.navController
        val navView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (navController != null) {
            NavigationUI.setupWithNavController(navView, navController, false)
        }
    }
}
