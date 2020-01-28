package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_average_cycle.*
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.getDayAddition
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import java.util.*


class AverageCycleFragment : AbstractQuizFragment() {

    private lateinit var choose: TextView
    private lateinit var days: TextView
    private lateinit var averageCyclePicker: NumberPicker

    private lateinit var pref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_average_cycle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        averageCyclePicker = view.findViewById<NumberPicker>(R.id.averageCyclePicker)
        choose = view.findViewById(R.id.choose)
        days = view.findViewById(R.id.days)
        averageCyclePicker.minValue = 0
        averageCyclePicker.maxValue = 40
        averageCyclePicker.setDividerColorResource(android.R.color.transparent)
        averageCyclePicker.setTextColorResource(R.color.colorGreyFont)
        averageCyclePicker.setTextSize(R.dimen.text_size_picker)
        averageCyclePicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
        averageCyclePicker.setSelectedTextSize(R.dimen.text_size_picker)
        averageCyclePicker.setOnValueChangedListener { _, _, _ ->
            choose.isVisible = false
            days.isVisible = true
            days.setText(averageCyclePicker.value.getDayAddition(context!!))
        }
        updateLocale()

    }

    override fun getStep(): Int = 2

    override fun getNextFragment(): AbstractQuizFragment? {
        return BirthDateFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return AverageMenstruationFragment()
    }

    override fun setQuizAns(cycle: Cycle) {
        if (!choose.isVisible) {
            cycle.lengthOfCycle = averageCyclePicker.value
            Log.d(QuizActivity.TAG, "Saving cycle length: ${cycle.lengthOfCycle}")
        }
    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        choose.setText(R.string.choose)
    }
}
