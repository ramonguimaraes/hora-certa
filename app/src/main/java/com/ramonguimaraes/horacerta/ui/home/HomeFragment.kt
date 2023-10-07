package com.ramonguimaraes.horacerta.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUiSaveStateControl
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ramonguimaraes.horacerta.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @OptIn(NavigationUiSaveStateControl::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.homeMainContainer) as NavHostFragment?
        val navController = navHostFragment?.navController
        val navView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (navController != null) {
            NavigationUI.setupWithNavController(navView, navController, false)
        }

        navView.setOnItemSelectedListener {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            }
            true
        }
    }
}
