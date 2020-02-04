package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.domain.notifications.NotifyWorker
import ru.brainstorm.android.womenscalendar.domain.predictor.PredictorImpl
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


public class NotificationsFragment : AbstractMenuFragment() {

    val NOTIFICATION_CHANNEL_ID = "10001"
    private val default_notification_channel_id = "default"
    private lateinit var notificationsButton : ImageView
    private lateinit var notificationStartMenstruationButton : ImageButton
    private lateinit var notificationEndMenstruationButton : ImageButton
    private lateinit var notificationOvulationButton : ImageButton
    private lateinit var notificationOpeningOfFertilityWindowButton : ImageButton
    private lateinit var notificationClosingOfFertilityWindowButton : ImageButton
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

    final val NotificationIDTag =  "START_MENSTRUATION_NOTIFICATION"
    final val NotificationMessageTag = "START_MENSTRUATION_NOTIFICATION_MESSAGE"
    private lateinit var requestId : UUID
    private val requestStartMenstruationKey = "requestStartMenstruationKey"
    private val requestEndMenstruationKey = "requestEndMenstruationKey"
    private val requestOvulationKey = "requestOvulationKey"
    private val requestOpenFertilityWindowKey = "requestOpenFertilityWindowKey"
    private val requestCloseFertilityWindowKey = "requestCloseFertilityWindowKey"
    

    fun scheduleNotification(message : String, startLocalDate : LocalDate, time : String, interval : Int,
                            isChecked : Boolean, requestKey : String, notificationId : Int){
        val time_ = time.parseDate()
        val startDate = Date( startLocalDate.year - 1900, startLocalDate.monthValue-1,
            startLocalDate.dayOfMonth - 1, time_.first, time_.second)
        val date1970 = Date(70, 0,0,0,0)
        val startTime = startDate.time - date1970.time - TimeZone.getDefault().getOffset(Date().time)

        val intent = Intent(context!!, NotificationStartOfMenstruationReceiver::class.java)
        intent.putExtra("message", message)
        val pendingIntent = PendingIntent.getBroadcast(context!!, 0, intent, 0)

        val alarmManager = context!!.getSystemService(ALARM_SERVICE) as AlarmManager

        if(isChecked) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                startTime,
                interval.toLong(),
                pendingIntent
            )

            val editor = pref.edit()
            editor.putString(requestKey, notificationId.toString())
            editor.commit()
        }else{
            alarmManager.cancel(pendingIntent)

            val editor = pref.edit()
            editor.putString(requestKey,"")
            editor.commit()
        }
    }
    @Inject
    lateinit var cycleDao: CycleDao
    var cycles = listOf<Cycle>()

    @Inject
    lateinit var predictorImpl : PredictorImpl

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
        App.appComponent.inject(this)
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                cycles = cycleDao.getAll()
            }
            job.join()
        }
        pref = PreferenceManager.getDefaultSharedPreferences(context)

        createNotificationChannel()

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
        notificationOvulationButton = view.findViewById<ImageButton>(R.id.btn_ovulation_notification)
        notificationOpeningOfFertilityWindowButton = view.findViewById<ImageButton>(R.id.btn_open_fetilnost_notification)
        notificationClosingOfFertilityWindowButton = view.findViewById<ImageButton>(R.id.close_fetilnost_notofication)


        //---> values for switch buttons <---
        switchStartMenstruationButton = view.findViewById<Switch>(R.id.switch_start_of_menstruation)
        switchEndMenstruationButton = view.findViewById<Switch>(R.id.switch_end_of_menstruation)
        switchOvulationButton = view.findViewById<Switch>(R.id.switch_ovulation)
        switchOpenFetilnostButton = view.findViewById<Switch>(R.id.switch_open_fetilnost)
        switchCloseFetilnostButton = view.findViewById<Switch>(R.id.switch_closing_ftilnost)

        //<--------------------------------->

        updateSwitchers()


        //---> switching notifications <---

        switchStartMenstruationButton.setOnCheckedChangeListener { _, isChecked ->

                scheduleNotification(pref.getString(MenstruationStartNotificationFragment().TextOfStartOfMenstruationNotificationTag,"This is start of your menstruation")!!,
                    FindStartOfMenstruation(cycles),
                    pref.getString(MenstruationStartNotificationFragment().TimeOfStartOfMenstruationNotificationTag, "9:00")!!,
                    FindCurrent(cycles).lengthOfCycle*24*60*60,
                    isChecked,
                    requestStartMenstruationKey,
                    1
                    )
        }
        //
        switchEndMenstruationButton.setOnCheckedChangeListener { _, isChecked ->
                val workRequestId = scheduleNotification(pref.getString(MenstruationEndNotificationFragment().TextOfEndOfMenstruationNotificationTag, "This is start of your menstruation")!!,
                    FindEndOfMenstruation(cycles),
                    pref.getString(MenstruationEndNotificationFragment().TimeOfEndOfMenstruationNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt(),
                    isChecked,
                    requestEndMenstruationKey,
                    2)


        }

        switchOvulationButton.setOnCheckedChangeListener { _, isChecked ->
                val workRequestId = scheduleNotification(pref.getString(OvulationNotificationFragment().TextOfOvulationNotificationTag, "This is start of your menstruation")!!,
                    FindOvulation(cycles),
                    pref.getString(OvulationNotificationFragment().TimeOfOvulationNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt(),
                    isChecked,
                    requestOvulationKey,
                    3)

        }

        switchOpenFetilnostButton.setOnCheckedChangeListener { _, isChecked ->
                val workRequestId = scheduleNotification(pref.getString(OpeningOfFertilityWindowNotificationFragment().TextOfOpeningOfFertilityWindowNotificationTag, "This is start of your menstruation")!!,
                    FindOpenOfFertilnost(cycles),
                    pref.getString(OpeningOfFertilityWindowNotificationFragment().TimeOfOpeningOfFertilityWindowNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt(),
                    isChecked,
                    requestOpenFertilityWindowKey,
                    4)


        }

        switchCloseFetilnostButton.setOnCheckedChangeListener { _, isChecked ->

                val workRequestId = scheduleNotification(pref.getString(ClosingOfFertilityWindowNotificationFragment().TextOfClosingOfFertilityWindowNotificationTag, "This is start of your menstruation")!!,
                    FindEndOfFertilnost(cycles),
                    pref.getString(ClosingOfFertilityWindowNotificationFragment().TimeOfClosingOfFertilityWindowNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt(), isChecked,
                    requestCloseFertilityWindowKey,
                    5)
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
        notificationOvulationButton.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@NotificationsFragment)
                menuPresenter.setFragment(supportFragmentManager, "ovulation")
            }
        }
        notificationOpeningOfFertilityWindowButton.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@NotificationsFragment)
                menuPresenter.setFragment(supportFragmentManager, "opening_of_fertility_window")
            }
        }
        notificationClosingOfFertilityWindowButton.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@NotificationsFragment)
                menuPresenter.setFragment(supportFragmentManager, "closing_of_fertility_window")
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

    fun updateSwitchers(){
        val requestStartMenstruationId = pref.getString(requestStartMenstruationKey, "")
        val requestEndMenstruationId = pref.getString(requestEndMenstruationKey, "")
        val requestOvulationId = pref.getString(requestOvulationKey, "")
        val requestOpenFertilityWindowId = pref.getString(requestOpenFertilityWindowKey, "")
        val requestCloseFertilityWindowId = pref.getString(requestCloseFertilityWindowKey, "")

        if(requestStartMenstruationId != "")
            switchStartMenstruationButton.isChecked = true

        if(requestEndMenstruationId != "")
            switchEndMenstruationButton.isChecked = true

        if(requestOvulationId != "")
            switchOvulationButton.isChecked = true

        if(requestOpenFertilityWindowId != "")
            switchOpenFetilnostButton.isChecked = true

        if(requestCloseFertilityWindowId != "")
            switchCloseFetilnostButton.isChecked = true

    }

    private fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "WomensCalendarNotificationChannel"
            val description = "Channel for Womens Calendar notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notifyWomensCalendar", name, importance)
            channel.description = description

            val notificationManager = context!!.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}
