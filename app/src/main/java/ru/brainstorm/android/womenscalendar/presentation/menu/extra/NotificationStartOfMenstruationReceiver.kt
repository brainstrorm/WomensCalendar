package ru.brainstorm.android.womenscalendar.presentation.menu.extra

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.brainstorm.android.womenscalendar.R

class NotificationStartOfMenstruationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context!!, "notifyWomensCalendar")
        val message = intent!!.getStringExtra("message")
        builder.setContentTitle("Women's Calendar")
        builder.setContentText(message)
        builder.setSmallIcon(R.drawable.app_icon)
        builder.setAutoCancel(true)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(200, builder.build())
    }
}