package com.ramonguimaraes.horacerta.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.utils.NotificationConstants.Companion.NOTIFICATION_CHANNEL
import com.ramonguimaraes.horacerta.utils.NotificationConstants.Companion.NOTIFICATION_ID

class NotificationConstants{
    companion object {
        const val NOTIFICATION_CHANNEL = "hora-certa-notification-channel"
        const val NOTIFICATION_ID = 123
    }
}

fun NotificationManager.sendNotification(title: String, body: String, context: Context) {
    val builder = NotificationCompat.Builder(
        context,
        NOTIFICATION_CHANNEL
    )

    builder.setSmallIcon(R.drawable.ic_alarm)
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .priority = NotificationCompat.PRIORITY_HIGH

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
