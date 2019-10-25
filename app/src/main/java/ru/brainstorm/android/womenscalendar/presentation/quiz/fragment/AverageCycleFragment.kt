package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.shawnlin.numberpicker.NumberPicker
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity


class AverageCycleFragment : AbstractQuizFragment() {

    private lateinit var choose: TextView
    private lateinit var days: TextView
    private lateinit var averageMenstruationPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_average_cycle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        averageMenstruationPicker = view.findViewById<NumberPicker>(R.id.averageCyclePicker)
        choose = view.findViewById(R.id.choose)
        days = view.findViewById(R.id.days)
        averageMenstruationPicker.minValue = 0
        averageMenstruationPicker.maxValue = 40
        averageMenstruationPicker.setDividerColorResource(android.R.color.transparent)
        averageMenstruationPicker.setTextColorResource(R.color.colorGreyFont)
        averageMenstruationPicker.setTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
        averageMenstruationPicker.setSelectedTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setOnValueChangedListener { _, _, _ ->
            choose.isVisible = false
            days.isVisible = true
        }

    }

    override fun getStep(): Int = 2

    override fun getNextFragment(): AbstractQuizFragment? {
        return BirthDateFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return AverageMenstruationFragment()
    }

    override fun setQuizAns(cycle: Cycle) {
        cycle.lengthOfCycle = averageMenstruationPicker.value
        Log.d(QuizActivity.TAG, "Saving cycle length: ${cycle.lengthOfCycle}")
    }
}
