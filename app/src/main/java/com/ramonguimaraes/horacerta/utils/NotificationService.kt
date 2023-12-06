package com.ramonguimaraes.horacerta.utils

import android.app.NotificationManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("TAG", "From: ${message.from}")
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val data = message.data

        when (data["notificationType"]) {
            "newSchedule" -> {

                val formatedNew = data["newDate"]?.toLong()?.let {
                    val date = Date(it)
                    formatDate("dd MMM yyyy 'às' hh'h'mm", date)
                } ?: ""

                val body = if (formatedNew.isNotBlank()) {
                    "Novo agendamento para $formatedNew"
                } else {
                    "verifique sua agenda"
                }

                notificationManager.sendNotification(
                    "Novo agendamento",
                    body,
                    applicationContext
                )
            }

            "reschedule" -> {

                val formatedOld = data["oldDate"]?.toLong()?.let {
                    val date = Date(it)
                    formatDate("dd MMM yyyy 'às' hh'h'mm", date)
                } ?: ""

                val formatedNew = data["newDate"]?.toLong()?.let {
                    val date = Date(it)
                    formatDate("dd MMM yyyy 'às' hh'h'mm", date)
                } ?: ""

                val body = if (formatedOld.isNotBlank() && formatedNew.isNotBlank()) {
                    "Reagendado de $formatedOld para $formatedNew"
                } else {
                    "verifique sua agenda"
                }

                notificationManager.sendNotification(
                    "Horario remacado",
                    body,
                    applicationContext
                )
            }

            "cancelSchedule" -> {

                val formatedOld = data["deletedDate"]?.toLong()?.let {
                    val date = Date(it)
                    formatDate("dd MMM yyyy 'às' hh'h'mm", date)
                } ?: ""

                val body = if (formatedOld.isNotBlank()) {
                    "Horario do dia $formatedOld foi cancelado"
                } else {
                    "verifique sua agenda"
                }

                notificationManager.sendNotification(
                    "Horario cancelado",
                    body,
                    applicationContext
                )
            }
        }
    }

    private fun formatDate(pattern: String, date: Date): String {
        val format = SimpleDateFormat(pattern, Locale("pt", "BR"))
        return format.format(date)
    }
}