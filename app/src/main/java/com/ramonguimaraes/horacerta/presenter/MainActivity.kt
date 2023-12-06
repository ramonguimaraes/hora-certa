package com.ramonguimaraes.horacerta.presenter

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.ActivityMainBinding
import com.ramonguimaraes.horacerta.utils.NotificationConstants


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    Log.d("NOTIFICATION_PERMISSION", "PERMISSION GRANTE")
                    createChannel()
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        Log.d("NOTIFICATION_PERMISSION", it)
                    }
                } else {
                    Log.d("NOTOFICAITON_PERMISSION", "PERMISSION DENIED")
                }
            }

        permissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            NotificationConstants.NOTIFICATION_CHANNEL,
            "Hora certa",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun logout() {
        val navController = findNavController(this, R.id.fragmentContainerView)
        navController.navigate(R.id.action_homeFragment_to_loginFragment)
    }
}