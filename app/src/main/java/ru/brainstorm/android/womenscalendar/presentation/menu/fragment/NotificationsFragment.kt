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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.app.NotificationCompat
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
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.parseDate
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

     fun scheduleNotification(message : String, startLocalDate : LocalDate, time : String, interval : Int){
         val time_ = time.parseDate()
         val startDate = Date( startLocalDate.year - 1900, startLocalDate.monthValue-1,
             startLocalDate.dayOfMonth - 1, time_.first, time_.second)
         val date1970 = Date(70, 0,0,0,0)
         val startTime = startDate.time - date1970.time - System.currentTimeMillis() - TimeZone.getDefault().getOffset(Date().time)

         val cycle = FindDate(cycles)

         //we set a tag to be able to cancel all work of this type if needed
         val workTag = "notificationWork";

         //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
         val inputData = Data.Builder()
             .putInt(NotificationIDTag, 1)
             .putString(NotificationMessageTag, message)
             .build()
         // we then retrieve it inside the NotifyWorker with:
         // final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

         val notificationWork = PeriodicWorkRequest.Builder(NotifyWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
             .setInitialDelay(startTime, TimeUnit.MILLISECONDS)
             .setInputData(inputData)
             .addTag(workTag)
             .build()

         requestId = notificationWork.id


         WorkManager.getInstance(context!!).enqueue(notificationWork)
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




        //---> switching notifications <---

        switchStartMenstruationButton.setOnCheckedChangeListener { _, isChecked ->

            val FLAG = "StartMenstruation"
            if(isChecked) {
                Log.d("Switcher", "Switcher on + ${Date()}")
                scheduleNotification(pref.getString(MenstruationStartNotificationFragment().TextOfStartOfMenstruationNotificationTag,
                    "This is start of your menstruation")!!,
                    LocalDate.now(),
                    pref.getString(MenstruationStartNotificationFragment().TimeOfStartOfMenstruationNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt())

                val editor = pref.edit()
                editor.putBoolean(FLAG,true)
                editor.commit()

            }else{

                WorkManager.getInstance(context!!).cancelWorkById(requestId)

                val editor = pref.edit()
                editor.putBoolean(FLAG,false)
                editor.commit()
            }

        }

        switchEndMenstruationButton.setOnCheckedChangeListener { _, isChecked ->

            val FLAG = "EndMenstruation"
            if(isChecked) {
                Log.d("Switcher", "Switcher on + ${Date()}")
                scheduleNotification(pref.getString(MenstruationEndNotificationFragment().TextOfEndOfMenstruationNotificationTag, "This is start of your menstruation")!!,
                    LocalDate.now(),
                    pref.getString(MenstruationEndNotificationFragment().TimeOfEndOfMenstruationNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt())
                val editor = pref.edit()
                editor.putBoolean(FLAG,true)
                editor.commit()
            }else{
                val editor = pref.edit()
                editor.putBoolean(FLAG,false)
                editor.commit()
            }


        }

        switchOvulationButton.setOnCheckedChangeListener { _, isChecked ->

            val FLAG = "Ovulation"
            if(isChecked) {
                Log.d("Switcher", "Switcher on + ${Date()}")
                scheduleNotification(pref.getString(OvulationNotificationFragment().TextOfOvulationNotificationTag, "This is start of your menstruation")!!,
                    LocalDate.now(),
                    pref.getString(OvulationNotificationFragment().TimeOfOvulationNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt())
                val editor = pref.edit()
                editor.putBoolean(FLAG,true)
                editor.commit()
            }else{
                val editor = pref.edit()
                editor.putBoolean(FLAG,false)
                editor.commit()
            }

        }

        switchOpenFetilnostButton.setOnCheckedChangeListener { _, isChecked ->

            val FLAG = "OpenFetilnost"
            if(isChecked) {
                Log.d("Switcher", "Switcher on + ${Date()}")
                    // scheduleNotification(pref.getString(OpeningOfFertilityWindowNotificationFragment().TextOfOpeningOfFertilityWindowNotificationTag, "This is start of your menstruation")!!,
                    //LocalDate.now(),
                    //pref.getString(OpeningOfFertilityWindowNotificationFragment().TimeOfOpeningOfFertilityWindowNotificationTag, "9:00")!!,
                    //2*AlarmManager.INTERVAL_DAY.toInt())
                val editor = pref.edit()
                editor.putBoolean(FLAG,true)
                editor.commit()
            }else{
                val editor = pref.edit()
                editor.putBoolean(FLAG,false)
                editor.commit()
            }

        }

        switchCloseFetilnostButton.setOnCheckedChangeListener { _, isChecked ->

            val FLAG = "CloseFetilnost"
            if (isChecked) {
                Log.d("Switcher", "Switcher on + ${Date()}")
                scheduleNotification(pref.getString(ClosingOfFertilityWindowNotificationFragment().TextOfClosingOfFertilityWindowNotificationTag, "This is start of your menstruation")!!,
                    LocalDate.now(),
                    pref.getString(ClosingOfFertilityWindowNotificationFragment().TimeOfClosingOfFertilityWindowNotificationTag, "9:00")!!,
                    2*AlarmManager.INTERVAL_DAY.toInt())
                val editor = pref.edit()
                editor.putBoolean(FLAG, true)
                editor.commit()
            } else {
                val editor = pref.edit()
                editor.putBoolean(FLAG, false)
                editor.commit()

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

        var ans = 0

        for(i in 1..set_update.size-1) {

            if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) <= 0) {
                if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle).plusDays(set_update[i].lengthOfCycle.toLong())) >= 0) {
                    ans = i-1
                }
            }
        }

        return set_update[ans]
    }
}
