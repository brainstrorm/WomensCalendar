package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.app.NotificationCompat
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.domain.notifications.NotificationReceiver
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.util.*
import javax.inject.Inject


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

    private lateinit var txtvwPeriodicNotifications : TextView
    private lateinit var txtvwStartOfMenstruation : TextView
    private lateinit var txtvwMenstruationBeginsToday : TextView
    private lateinit var txtvwEndOfMenstruation : TextView
    private lateinit var txtvwNoteEndOfMenstruation : TextView
    private lateinit var txtvwOpenOfFertility : TextView
    private lateinit var txtvwFertilityWindow : TextView
    private lateinit var txtvwOvulation : TextView
    private lateinit var txtvwPredictableOvulation : TextView
    private lateinit var txtvwClosingOfFertilityWindow : TextView
    private lateinit var txtvwFertilityWindowCloses : TextView

    private lateinit var pref : SharedPreferences

     fun scheduleNotification(pendingIntent: PendingIntent, delay: Int, interval : Int){
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = SystemClock.elapsedRealtime()
        calendar.set(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 10)
        calendar.set(Calendar.HOUR, 19)
        calendar.set(Calendar.MINUTE, 58)
        calendar.set(Calendar.SECOND, 0)
        val futureInMillis = SystemClock.elapsedRealtime()
        val alarmManager =
            (context!!.getSystemService(ALARM_SERVICE) as AlarmManager?)!!
        //alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
        alarmManager.cancel(pendingIntent)
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.timeInMillis, 2*AlarmManager.INTERVAL_DAY, pendingIntent)
    }
    @Inject
    lateinit var cycleDao: CycleDao
    var cycles = listOf<Cycle>()


     fun getPendingIntent(notification: Notification) : PendingIntent{
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

    fun getNotification(content: String): Notification? {
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

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        txtvwPeriodicNotifications = view.findViewById(R.id.period_notification)
        txtvwStartOfMenstruation = view.findViewById(R.id.start_of_menstruation)
        txtvwMenstruationBeginsToday = view.findViewById(R.id.menstruation_begins_today)
        txtvwEndOfMenstruation = view.findViewById(R.id.end_of_menstruation)
        txtvwNoteEndOfMenstruation = view.findViewById(R.id.dont_forget_note_end_of_menstruation)
        txtvwOpenOfFertility = view.findViewById(R.id.opening_of_fertility)
        txtvwFertilityWindow = view.findViewById(R.id.fertility_window)
        txtvwOvulation = view.findViewById(R.id.ovulation)
        txtvwPredictableOvulation = view.findViewById(R.id.predictable_ovulation)
        txtvwClosingOfFertilityWindow = view.findViewById(R.id.close_of_fertility_window)
        txtvwFertilityWindowCloses = view.findViewById(R.id.closing_of_fertility_window)

        updateLocale()

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

            val pendingIntent = getPendingIntent(getNotification("Hello World!")!!)
            val FLAG = "StartMenstruation"
            if(isChecked) {
                scheduleNotification(pendingIntent, CalculateDelay(FindDate(cycles).startOfCycle), CalculatePeriod(FindDate(cycles).lengthOfCycle))
                val editor = pref.edit()
                editor.putBoolean(FLAG,true)

            }else{
                val alarmManager =
                    (context!!.getSystemService(ALARM_SERVICE) as AlarmManager?)!!
                alarmManager.cancel(pendingIntent)
                val editor = pref.edit()
                editor.putBoolean(FLAG,false)

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

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwPeriodicNotifications.setText(R.string.period_notifications)
        txtvwStartOfMenstruation.setText(R.string.start_of_menstruation)
        txtvwMenstruationBeginsToday.setText(R.string.menstruation_starts_today)
        txtvwEndOfMenstruation.setText(R.string.end_of_menstruation)
        txtvwNoteEndOfMenstruation.setText(R.string.do_not_forget)
        txtvwOpenOfFertility.setText(R.string.open_fertilnost)
        txtvwFertilityWindow.setText(R.string.open_fertilnost_today)
        txtvwOvulation.setText(R.string.ovulation_notifications)
        txtvwPredictableOvulation.setText(R.string.ovulation_will_stsrt)
        txtvwClosingOfFertilityWindow.setText(R.string.end_fertilnost)
        txtvwFertilityWindowCloses.setText(R.string.window_of_fertilnost_is_closing)
    }

    fun CalculateDelay(startOfCycle: String):Int {
        val duringDate = java.time.LocalDate.now()
        val newDate = java.time.LocalDate.parse(startOfCycle)
        val newDays = newDate.dayOfYear - duringDate.dayOfYear

        return newDays*24*60*60*1000
    }

    fun CalculatePeriod(lengthOfCycle : Int):Int {
        return lengthOfCycle*24*60*60*1000
    }

    fun FindDate(set_update: List<Cycle>): Cycle {
        val date = java.time.LocalDate.now()

        var ans = set_update.size-1

        for(i in 0..set_update.size-2) {

            if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) <= 0) {
                if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle+set_update[i].lengthOfCycle)) >= 0) {
                    ans = i+1
                }
            }
        }

        return set_update[ans]
    }
}
