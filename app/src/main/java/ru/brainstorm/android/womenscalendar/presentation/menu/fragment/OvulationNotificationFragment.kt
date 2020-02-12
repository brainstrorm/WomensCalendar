package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.*
import java.util.*
import javax.inject.Inject

class OvulationNotificationFragment : AbstractMenuFragment(), OnBackPressedListener {
    private lateinit var mainView : View
    private lateinit var backButton : ImageView
    private lateinit var timeLayout : ConstraintLayout
    private lateinit var timeInfoLayout: ConstraintLayout
    private lateinit var directTimeTextView : TextView
    private lateinit var timePicker : TimePicker
    private lateinit var messageEditText: EditText
    private lateinit var pref : SharedPreferences

    private lateinit var txtvwTime : TextView
    private lateinit var txtvwMessage: TextView

    @Inject
    lateinit var cycleDao: CycleDao
    var cycles = listOf<Cycle>()

    companion object{
        val TAG = "ovulation"
        val TimeOfOvulationNotificationTag = "TimeOfOvulationNotification"
        val TextOfOvulationNotificationTag : String = "TextOfOvulationNotification"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_ovulation_notification, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        App.appComponent.inject(this)

        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                cycles = cycleDao.getAll()
            }
            job.join()
        }

        initViews()
        initAnimators()

        updateLocale()

        return mainView
    }

    private fun initViews(){
        timeLayout = mainView!!.findViewById<ConstraintLayout>(R.id.notification_ovulation_time)
        timeInfoLayout = mainView!!.findViewById<ConstraintLayout>(R.id.ovulation_pick_time)
        timeInfoLayout.findViewById<Button>(R.id.ovulation_cancel_notification)
            .setOnClickListener{rollUpTimePicker()}
        timeInfoLayout.findViewById<Button>(R.id.ovulation_save_notification)
            .setOnClickListener{rollUpTimePicker(true)}
        messageEditText = mainView.findViewById<EditText>(R.id.message_edit)

        backButton = activity!!.findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener{view ->
            (activity as MenuActivity).apply {
                if (messageEditText.text.toString() != resources.getString(R.string.ovulation_will_stsrt_message)) {
                    if (messageEditText.text.toString() != (resources.getString(R.string.ovulation_will_stsrt_message) + " ")) {

                        pref.edit()
                            .putString(
                                TextOfOvulationNotificationTag,
                                messageEditText.text.toString()
                            )
                            .commit()

                    }
                }
                var isChecked = false
                if(pref.getString(NotificationsFragment.requestOvulationKey, "") != ""){
                    isChecked = true
                }
                scheduleNotification(pref.getString(OvulationNotificationFragment.TextOfOvulationNotificationTag,"This is start of your menstruation")!!,
                    FindOvulation(cycles),
                    pref.getString(OvulationNotificationFragment.TimeOfOvulationNotificationTag, "9:00")!!,
                    FindCurrent(cycles).lengthOfCycle.toLong()*24*60*60*1000,
                    isChecked,
                    NotificationsFragment.requestOvulationKey,
                    1
                )
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }

        directTimeTextView = mainView!!.findViewById<TextView>(R.id.direct_time_text)
        directTimeTextView.text = pref.getString(TimeOfOvulationNotificationTag,"9:00")!!.addZeros()


        timePicker = mainView!!.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        txtvwTime = mainView!!.findViewById<TextView>(R.id.time_text)
        txtvwMessage = mainView!!.findViewById<TextView>(R.id.message)
    }

    private fun initAnimators() {


        timeLayout.setOnClickListener {
            val time_height = timeLayout.height*6

            val heightAnimator = ValueAnimator.ofInt(0, time_height).setDuration(1_000)
            heightAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                timeInfoLayout.layoutParams.height = value
                timeInfoLayout.requestLayout()
            }

            val set = AnimatorSet()
            set.play(heightAnimator)
            set.interpolator = AccelerateDecelerateInterpolator()
            set.start()
        }
    }

    private fun rollUpTimePicker(save: Boolean = false) {
        if (save) {
            val saved = "${timePicker.hour}:${timePicker.minute}"
            directTimeTextView.text = saved.addZeros()
            pref.edit().putString("time_ovulation",saved).apply()

            val editor = pref.edit()
            editor.putString(TimeOfOvulationNotificationTag, saved)
            editor.commit()
        }
        val height = activity!!.windowManager.defaultDisplay.height

        val time_height = timeLayout.height*6
        val heightAnimator = ValueAnimator.ofInt(time_height, 0).setDuration(1_000)
        heightAnimator.addUpdateListener {
            timeInfoLayout.layoutParams.height = it.animatedValue as Int
            timeInfoLayout.requestLayout()
        }
        val set = AnimatorSet()
        set.play(heightAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.start()
    }
    override fun getPart(): String = "ovulation"

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)//
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwTime.setText(R.string.time)
        txtvwMessage.setText(R.string.message)
        messageEditText.setText(pref.getString(TextOfOvulationNotificationTag,activity!!.resources.getString(R.string.ovulation_will_stsrt_message)))

    }

    override fun onBackPressed() {
        (activity as MenuActivity).apply {
            menuPresenter.popBackStack(supportFragmentManager)
            pref.edit()
                .putString(TextOfOvulationNotificationTag, messageEditText.text.toString())
                .commit()
        }
    }

    fun scheduleNotification(message : String, startLocalDate : LocalDate, time : String, interval : Long,
                             isChecked : Boolean, requestKey : String, notificationId : Int){
        val time_ = time.parseDate()
        val startDate = Date( startLocalDate.year - 1900, startLocalDate.monthValue-1,
            startLocalDate.dayOfMonth - 1, time_.first, time_.second)
        val date1970 = Date(70, 0,0,0,0)
        val startTime = startDate.time - date1970.time - TimeZone.getDefault().getOffset(Date().time)

        val intent = Intent(context!!, NotificationStartOfMenstruationReceiver::class.java).apply {
            putExtra("message", message)
        }
        //activity!!.sendBroadcast(intent)
        val s = message
        val pendingIntent = PendingIntent.getBroadcast(context!!, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)

        if(isChecked) {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                startTime,
                interval,
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

}
