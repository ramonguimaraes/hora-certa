package com.ramonguimaraes.horacerta.presenter

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun logout() {
        val navController = findNavController(this, R.id.fragmentContainerView)
        navController.navigate(R.id.action_homeFragment_to_loginFragment)
    }
}