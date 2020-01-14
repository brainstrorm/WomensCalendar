package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.rate_us.activity.RateUsActivity
import java.util.*
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 06.11.2019
 */
class SettingsFragment
    : AbstractMenuFragment() {

    @Inject
    lateinit var cycleDao: CycleDao

    private lateinit var mainView: View
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

    private lateinit var pref : SharedPreferences

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
        mainView = inflater.inflate(R.layout.fragment_settings, container, false)
        val kek: View = mainView.findViewById(R.id.main)
        (mainView as ViewGroup).removeView(kek)
        mainView = kek
        initViews()
        initAnimators()

        txtvwNotifications = mainView.findViewById(R.id.notifications_text)
        txtvwGraphicsAndReports = mainView.findViewById(R.id.graphics_and_reports_text)
        txtvwSettings = mainView.findViewById(R.id.settings_text)
        txtvwRateUs = mainView.findViewById(R.id.rate_us_text)
        txtvwHelpAndFeedback = mainView.findViewById(R.id.help_and_feedback_text)
        txtvwDeleteAllNotes = mainView.findViewById(R.id.delete_all_notes_text)
        txtvwAboutApp = mainView.findViewById(R.id.about_app)

        pref = PreferenceManager.getDefaultSharedPreferences(context)
        updateLocale()
        return mainView
    }

    private fun initViews() {
        deleteAllNotes = mainView.findViewById(R.id.deleteAll)
        deleteAllNotes.setOnClickListener {
            Toast.makeText(context, R.string.delete_all_notes, Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                val all = cycleDao.getAll()
                all.forEach { cycleDao.delete(it) }
            }
        }
        statistics = mainView.findViewById(R.id.graphs)
        rate_us=mainView.findViewById(R.id.rate_us_text)

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
            val cycle = cycleDao.getLatest()
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
}