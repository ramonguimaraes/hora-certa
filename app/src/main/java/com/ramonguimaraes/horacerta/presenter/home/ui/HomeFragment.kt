package com.ramonguimaraes.horacerta.presenter.home.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentHomeBinding
import com.ramonguimaraes.horacerta.presenter.home.viewModel.HomeViewModel
import com.ramonguimaraes.horacerta.presenter.MainActivity
import com.ramonguimaraes.horacerta.domain.user.model.AccountType
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var navController: NavController
    private val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureNavigation()
        setupNavBottom()
        if (args.accountType == AccountType.CLIENT) {
            navigate(R.id.companies)
        }
    }

    private fun configureNavigation() {
        val host = childFragmentManager.findFragmentById(R.id.homeMainContainer) as NavHostFragment
        navController = host.navController
    }

    private fun setupNavBottom() {
        val bottomNavigation = binding.bottomNavigationView
        with(bottomNavigation) {
            this.menu.clear()
            this.inflateMenu(resolveMenu())
            this.setupWithNavController(navController)
            this.setOnItemSelectedListener { menuItem ->
                if (menuItem.itemId == R.id.logout) {
                    logout()
                    false
                } else {
                    navigate(menuItem.itemId)
                }
            }
        }
    }

    private fun resolveMenu(): Int {
        return when (args.accountType) {
            AccountType.COMPANY -> {
                R.menu.company_menu
            }

            AccountType.CLIENT -> {
                R.menu.client_menu
            }

            else -> {
                throw Exception("Account Type Invalid")
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
