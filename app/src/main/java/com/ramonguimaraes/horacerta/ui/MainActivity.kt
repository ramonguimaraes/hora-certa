package com.ramonguimaraes.horacerta.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}