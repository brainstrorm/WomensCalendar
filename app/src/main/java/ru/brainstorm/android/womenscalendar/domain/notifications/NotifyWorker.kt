package ru.brainstorm.android.womenscalendar.domain.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.NotificationsFragment
import java.util.*


class NotifyWorker(@NonNull context: Context, @NonNull params: WorkerParameters) :
    Worker(context, params) {
    @NonNull
    override
    fun doWork(): Result {
        triggerNotification()
        return Result.success()
    }

    private fun triggerNotification() {

        val womens_calendar_notification_channel = "Womens Calendar Event Reminder"

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //define the importance level of the notification
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            //build the actual notification channel, giving it a unique ID and name
            val channel = NotificationChannel(
                womens_calendar_notification_channel,
                womens_calendar_notification_channel,
                importance
            )
            //we can optionally add a description for the channel
            val description =
                "A channel which shows notifications about events at Womens Calendar"
            channel.description = description
            //we can optionally set notification LED colour
            channel.lightColor = Color.MAGENTA
            // Register the channel with the system
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        //create an intent to open the event details activity
        val intent = Intent(applicationContext, MenuActivity::class.java)


        //put together the PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            intent,
            FLAG_UPDATE_CURRENT
        )

        //get latest event details
        val notificationTitle = "Womens Calendar"
        val notificationText =
            inputData.getString(NotificationsFragment().NotificationMessageTag)

        //build the notification
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, womens_calendar_notification_channel)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //trigger the notification
        val notificationManager =
            NotificationManagerCompat.from(applicationContext)

        //we give each notification the ID of the event it's describing,
        //to ensure they all show up and there are no duplicates
        //we give each notification the ID of the event it's describing,
        //to ensure they all show up and there are no duplicates
        notificationManager.notify(0, notificationBuilder.build())
        Log.d("Notification", "Notification at ${Date()}")
    }

}