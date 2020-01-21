package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.ActivityNotFoundException
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
import kotlinx.coroutines.*
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.User
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.domain.predictor.PredictorImpl
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.rate_us.activity.RateUsActivity
import java.util.*
import javax.inject.Inject
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.*
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import android.content.DialogInterface



/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 06.11.2019
 */
class SettingsFragment
    : AbstractMenuFragment() {

    @Inject
    lateinit var cycleDao: CycleDao

    @Inject
    lateinit var noteDao: NoteDao

    @Inject
    lateinit var predictorImpl : PredictorImpl


    private lateinit var mainView: ScrollView
    private lateinit var menstInfoLayout: ConstraintLayout
    private lateinit var cycleInfoLayout: ConstraintLayout
    private lateinit var menstLayout: ConstraintLayout
    private lateinit var cycleLayout: ConstraintLayout
    private lateinit var menstPicker: com.shawnlin.numberpicker.NumberPicker
    private lateinit var cyclePicker: com.shawnlin.numberpicker.NumberPicker
    private lateinit var menstTextView: TextView
    private lateinit var cycleTextView: TextView
    private lateinit var rate_us: TextView
    private lateinit var txtvwNotifications : TextView
    private lateinit var txtvwGraphicsAndReports : TextView
    private lateinit var txtvwSettings : TextView
    private lateinit var txtvwRateUs : TextView
    private lateinit var txtvwHelpAndFeedback : TextView
    private lateinit var txtvwDeleteAllNotes : TextView
    private lateinit var txtvwAboutApp : TextView
    private lateinit var deleteAllNotes: LinearLayout
    private lateinit var statistics: LinearLayout
    private lateinit var settingsLayout : LinearLayout
    private lateinit var notificationsLayout : LinearLayout
    private lateinit var btnBack : ImageView
    private lateinit var txtvwValueOfMenstruation : TextView
    private lateinit var txtvwValueOfCycle : TextView

    private lateinit var pref : SharedPreferences

    private val MenstruationDurationTag = "MENSTRUATION_DURATION"
    private val CycleDurationTag = "CYCLE_DURATION"

    override fun getPart(): String = "more"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainView = inflater.inflate(R.layout.fragment_settings, container, false) as ScrollView
        initViews()
        initAnimators()

        txtvwNotifications = mainView.findViewById(R.id.notifications_text)
        txtvwGraphicsAndReports = mainView.findViewById(R.id.graphics_and_reports_text)
        txtvwSettings = mainView.findViewById(R.id.settings_text)
        txtvwRateUs = mainView.findViewById(R.id.rate_us_text)
        txtvwHelpAndFeedback = mainView.findViewById(R.id.help_and_feedback_text)
        txtvwDeleteAllNotes = mainView.findViewById(R.id.delete_all_notes_text)
        txtvwAboutApp = mainView.findViewById(R.id.about_app)
        txtvwAboutApp.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@SettingsFragment)
                menuPresenter.setFragment(supportFragmentManager, "about_app")
            }
        }



        txtvwHelpAndFeedback.setOnClickListener {
            sendEmail()
        }

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        updateLocale()
        updateInformation()


        return mainView

    }

    private fun initViews() {
    deleteAllNotes = mainView.findViewById(R.id.deleteAll)


    deleteAllNotes.setOnClickListener {

        val builder = AlertDialog.Builder(getActivity())
        builder.setMessage(R.string.are_you_sure)
            .setPositiveButton(R.string.yes_settings, DialogInterface.OnClickListener { dialog, id ->

                CoroutineScope(Dispatchers.IO).launch {
                    val allCycles = cycleDao.getAll()
                    val allNotes = noteDao.getAll()

                    allCycles.forEach { cycleDao.delete(it) }
                    allNotes.forEach { noteDao.delete(it) }

                    startActivity(QuizActivity.provideIntent(activity as MenuActivity))
                }

            })
            .setNegativeButton(R.string.no_settings, DialogInterface.OnClickListener { dialog, id ->

            })

        builder.create().show()

    }



        statistics = mainView.findViewById(R.id.graphs)
        rate_us = mainView.findViewById(R.id.rate_us_text)

        statistics.setOnClickListener {
            (activity as MenuActivity).goToStatistic()
        }
        rate_us.setOnClickListener{
            (activity as MenuActivity).goToRateUs()
        }

        menstInfoLayout = mainView.findViewById(R.id.expand_menstruation_picker)
        cycleInfoLayout = mainView.findViewById(R.id.expand_cycle_picker)
        menstTextView = menstInfoLayout.findViewById(R.id.valueOfMenstruation)
        cycleTextView = cycleInfoLayout.findViewById(R.id.valueOfCycle)
        runBlocking {
            val cycles = cycleDao.getAll()
            val cycle = FindCurrent(cycles)
            menstTextView.text = resources.getQuantityString(R.plurals.days, cycle.lengthOfMenstruation, cycle.lengthOfMenstruation)
            cycleTextView.text = resources.getQuantityString(R.plurals.days, cycle.lengthOfCycle, cycle.lengthOfCycle)
        }
        menstLayout = mainView.findViewById(R.id.settingsPickMenstruation)
        cycleLayout = mainView.findViewById(R.id.settingsPickCycle)
        menstLayout.findViewById<View>(R.id.settings_cancel_menstruation).setOnClickListener { rollUpMenstPicker() }
        menstLayout.findViewById<View>(R.id.settings_save_menstruation).setOnClickListener { rollUpMenstPicker(true) }
        cycleLayout.findViewById<View>(R.id.settings_cancel).setOnClickListener { rollUpCyclePicker() }
        cycleLayout.findViewById<View>(R.id.settings_save).setOnClickListener { rollUpCyclePicker(true) }
        menstPicker = menstLayout.findViewById(R.id.pick_menstruation)
        menstPicker.setDividerColorResource(android.R.color.transparent)
        menstPicker.setTextColorResource(R.color.colorGreyFont)
        menstPicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
        cycleLayout = mainView.findViewById(R.id.settingsPickCycle)
        cyclePicker = cycleLayout.findViewById(R.id.pick_cycle)
        cyclePicker.setDividerColorResource(android.R.color.transparent)
        cyclePicker.setTextColorResource(R.color.colorGreyFont)
        cyclePicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
        settingsLayout = mainView.findViewById<LinearLayout>(R.id.settings).apply { setOnClickListener{view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@SettingsFragment)
                menuPresenter.setFragment(supportFragmentManager, "languages") }
        } }
        notificationsLayout = mainView.findViewById<LinearLayout>(R.id.reminders).apply {
            setOnClickListener { view ->
                (activity as MenuActivity).apply{
                    menuPresenter.addFragmentToBackStack(this@SettingsFragment)
                    menuPresenter.setFragment(supportFragmentManager, "notifications")
                }
            }
        }
        btnBack = activity!!.findViewById(R.id.btn_back)
        btnBack.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
        cyclePicker.minValue = 0
        cyclePicker.maxValue = 30

        menstPicker.minValue = 0
        menstPicker.maxValue = 10
    }

    private fun initAnimators() {
        menstInfoLayout.setOnClickListener {
            val heightAnimator = ValueAnimator.ofInt(0, 400).setDuration(1_000)
            heightAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                menstLayout.layoutParams.height = value
                menstLayout.requestLayout()
            }
            val set = AnimatorSet()
            set.play(heightAnimator)
            set.interpolator = AccelerateDecelerateInterpolator()
            set.start()
        }
        cycleInfoLayout.setOnClickListener {
            val heightAnimator = ValueAnimator.ofInt(0, 400).setDuration(1_000)
            heightAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                cycleLayout.layoutParams.height = value
                cycleLayout.requestLayout()
            }
            val set = AnimatorSet()
            set.play(heightAnimator)
            set.interpolator = AccelerateDecelerateInterpolator()
            set.start()
        }
    }

    private fun rollUpMenstPicker(save: Boolean = false) {
        if (save) {
            val saved = menstPicker.value
            menstTextView.text = resources.getQuantityString(R.plurals.days, saved, saved)
            CoroutineScope(Dispatchers.IO).launch {
                val currentCycle = cycleDao.getById(FindCurrent(cycleDao.getAll()).id)
                val currentId = currentCycle.id
                currentCycle.lengthOfMenstruation = saved
                cycleDao.update(currentCycle)
                var cycles = cycleDao.getAll()
                var count = 0
                for (cycle in cycles){
                    if (currentId < cycle.id) {
                        count++
                        cycleDao.delete(cycle)
                    }
                }
                cycles = cycleDao.getAll()
                predictorImpl.predict(5, PreferenceManager.getDefaultSharedPreferences(context))
                predictorImpl.updateOvulation()
            }
            (activity as MenuActivity).apply {
                if(supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG) != null) {
                    supportFragmentManager.beginTransaction()
                        .remove(supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)!!)
                        .commitNow()
                }
            }
        }
        val heightAnimator = ValueAnimator.ofInt(400, 0).setDuration(1_000)
        heightAnimator.addUpdateListener {
            menstLayout.layoutParams.height = it.animatedValue as Int
            menstLayout.requestLayout()
        }
        val set = AnimatorSet()
        set.play(heightAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.start()
    }

    private fun rollUpCyclePicker(save: Boolean = false) {
        if (save) {
            val saved = cyclePicker.value
            cycleTextView.text = resources.getQuantityString(R.plurals.days, saved, saved)
            CoroutineScope(Dispatchers.IO).launch {
                var currentCycle = cycleDao.getById(FindCurrent(cycleDao.getAll()).id)
                val currentId = currentCycle.id
                currentCycle.lengthOfCycle = saved
                cycleDao.update(currentCycle)
                var cycles = cycleDao.getAll()
                var count = 0
                for (cycle in cycles){
                    if (currentId < cycle.id) {
                        count++
                        cycleDao.delete(cycle)
                    }
                }
                cycles = cycleDao.getAll()
                predictorImpl.predict(5, PreferenceManager.getDefaultSharedPreferences(context))
                predictorImpl.updateOvulation()
            }

            (activity as MenuActivity).apply {
                if(supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG) != null) {
                    supportFragmentManager.beginTransaction()
                        .remove(supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)!!)
                        .commit()
                }
            }
        }
        val heightAnimator = ValueAnimator.ofInt(400, 0).setDuration(1_000)
        heightAnimator.addUpdateListener {
            cycleLayout.layoutParams.height = it.animatedValue as Int
            cycleLayout.requestLayout()
        }
        val set = AnimatorSet()
        set.play(heightAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.start()
    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwNotifications.setText(R.string.remind)
        txtvwGraphicsAndReports.setText(R.string.graphs)
        txtvwSettings.setText(R.string.settings)
        txtvwRateUs.setText(R.string.rate_us)
        txtvwHelpAndFeedback.setText(R.string.help_feedback)
        txtvwDeleteAllNotes.setText(R.string.delete_all_notes)
        txtvwAboutApp.setText(R.string.about_us)
    }
    fun updateInformation(){
        runBlocking {
            val cycles = cycleDao.getAll()
            val cycle = FindCurrent(cycles)
            menstTextView.text = resources.getQuantityString(R.plurals.days, cycle.lengthOfMenstruation, cycle.lengthOfMenstruation)
            cycleTextView.text = resources.getQuantityString(R.plurals.days, cycle.lengthOfCycle, cycle.lengthOfCycle)
        }
    }
    fun sendEmail(){
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("mobappcloud@gmail.com"))
        //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email")
        //i.putExtra(Intent.EXTRA_TEXT, "body of email")
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //
}


