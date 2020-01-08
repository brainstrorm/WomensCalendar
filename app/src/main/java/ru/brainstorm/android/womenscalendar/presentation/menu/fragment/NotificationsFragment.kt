package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import androidx.core.app.NotificationCompat
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.domain.notifications.NotificationReceiver
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.util.*


public class NotificationsFragment : AbstractMenuFragment() {

    val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"
    private lateinit var notificationsButton : ImageView
    private lateinit var notificationStartMenstruationButton : ImageButton
    private lateinit var notificationEndMenstruationButton : ImageButton
    private lateinit var switchStartMenstruationButton : Switch
    private lateinit var switchEndMenstruationButton : Switch
    private lateinit var switchOpenFetilnostButton : Switch
    private lateinit var switchOvulationButton : Switch
    private lateinit var switchCloseFetilnostButton : Switch


    private fun scheduleNotification(notification: Notification, delay: Int) {
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 22)
        calendar.set(Calendar.MINUTE, 58)
        //val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager =
            (context!!.getSystemService(ALARM_SERVICE) as AlarmManager?)!!
        //alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun getNotification(content: String): Notification? {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context!!, default_notification_channel_id)
        builder.setContentTitle("Scheduled Notification")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.app_icon)
        builder.setAutoCancel(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        notificationsButton = activity!!.findViewById<ImageView>(R.id.btn_back)
        notificationStartMenstruationButton = view.findViewById<ImageButton>(R.id.btn_start_of_menstruation_notification)
        notificationEndMenstruationButton = view.findViewById<ImageButton>(R.id.btn_end_of_menstruation_notification)


        //---> values for switch buttons <---
        switchStartMenstruationButton = view.findViewById<Switch>(R.id.switch_start_of_menstruation)
        switchEndMenstruationButton = view.findViewById<Switch>(R.id.switch_end_of_menstruation)
        switchOvulationButton = view.findViewById<Switch>(R.id.switch_ovulation)
        switchOpenFetilnostButton = view.findViewById<Switch>(R.id.switch_open_fetilnost)
        switchCloseFetilnostButton = view.findViewById<Switch>(R.id.switch_closing_ftilnost)

        //<--------------------------------->




        //---> switching notifications <---

        switchStartMenstruationButton.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked) {


                /*var builder = Notification.Builder(context, "1")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Hello!")
                    .setContentText("It's a notificatiion")
                    //.setPriority(NotificationCompat.PRIORITY_DEFAULT)

                val notification = builder.build()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = "My channel"
                    val descriptionText = "Womens Calendar"
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel("1", name, importance).apply {
                        description = descriptionText
                    }
                    channel.enableVibration(true)
                    // Register the channel with the system
                    val notificationManager: NotificationManager =
                        context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)

                    notificationManager.notify(1,notification)
                }*/

                scheduleNotification(getNotification("Hello World!")!!, 10000)

            }

        }

        //<---------------------------------->

        notificationsButton.setOnClickListener{view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
        notificationStartMenstruationButton.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@NotificationsFragment)
                menuPresenter.setFragment(supportFragmentManager, "start_of_menstruation")
            }
        }
        notificationEndMenstruationButton.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@NotificationsFragment)
                menuPresenter.setFragment(supportFragmentManager, "end_of_menstruation")
            }
        }
        return view
    }

    override fun getPart(): String = "notifications"

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
