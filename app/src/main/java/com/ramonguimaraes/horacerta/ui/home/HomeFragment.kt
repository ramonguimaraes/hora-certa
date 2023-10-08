package com.ramonguimaraes.horacerta.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentHomeBinding
import com.ramonguimaraes.horacerta.presenter.home.HomeViewModel
import com.ramonguimaraes.horacerta.ui.MainActivity
import com.ramonguimaraes.horacerta.ui.authentication.LoginFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.log

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureNavigation()
    }

    private fun configureNavigation() {
        val host = childFragmentManager.findFragmentById(R.id.homeMainContainer) as NavHostFragment
        navController = host.navController
        val bottomNavigation = binding.bottomNavigationView

        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.logout) {
                logout()
                false
            } else {
                navigate(menuItem.itemId)
            }
        }
    }

    private fun logout() {
        AlertDialog.Builder(context).setTitle("Alerta").setMessage("Realmente deseja sair?")
            .setPositiveButton("Sim") { dialog, _ ->
                homeViewModel.logout()
                dialog.dismiss()
                (activity as MainActivity).logout()
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun navigate(destination: Int): Boolean {
        return if (!isCurrentFragment(destination)) {
            navController.popBackStack()
            navController.navigate(destination)
            true
        } else {
            false
        }
    }

    private fun isCurrentFragment(menuItemId: Int): Boolean {
        val currentDestination = navController.currentDestination
        return currentDestination?.id == menuItemId
    }
}
