package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.di.AppComponent
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 06.11.2019
 */
class SettingsFragment
    : Fragment() {

    @Inject
    lateinit var cycleDao: CycleDao

    private lateinit var mainView: View
    private lateinit var menstInfoLayout: ConstraintLayout
    private lateinit var cycleInfoLayout: ConstraintLayout
    private lateinit var menstLayout: ConstraintLayout
    private lateinit var cycleLayout: ConstraintLayout
    private lateinit var menstPicker: NumberPicker
    private lateinit var cyclePicker: NumberPicker
    private lateinit var menstTextView: TextView
    private lateinit var cycleTextView: TextView
    private lateinit var deleteAllNotes: LinearLayout
    private lateinit var statistics: LinearLayout

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
        statistics.setOnClickListener {
            (activity as MenuActivity).goToStatistic()
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
        cycleLayout = mainView.findViewById(R.id.settingsPickCycle)
        cyclePicker = cycleLayout.findViewById(R.id.pick_cycle)
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
}