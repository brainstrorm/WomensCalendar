package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.R.color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.shawnlin.numberpicker.NumberPicker
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity


class AverageMenstruationFragment : AbstractQuizFragment() {

    private lateinit var averageMenstruationPicker: NumberPicker

    private lateinit var choose: TextView
    private lateinit var days: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_average_menstruation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        averageMenstruationPicker = view.findViewById<NumberPicker>(R.id.averageMenstruationPicker)
        choose = view.findViewById(R.id.choose)
        days = view.findViewById(R.id.days)
        averageMenstruationPicker.minValue = 0
        averageMenstruationPicker.maxValue = 10
        averageMenstruationPicker.setDividerColorResource(color.transparent)
        averageMenstruationPicker.setTextColorResource(R.color.colorGreyFont)
        averageMenstruationPicker.setTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
        averageMenstruationPicker.setSelectedTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setOnValueChangedListener { _, _, _ ->
            choose.isVisible = false
            days.isVisible = true
        }
    }

    override fun getStep(): Int = 1

    override fun getNextFragment(): AbstractQuizFragment? {
        return AverageCycleFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return CalendarPickerForQuizFragment()
    }

    override fun setQuizAns(ans: QuizAnswers) {
        ans.averageTimeOfMenstruation = averageMenstruationPicker.value
        Log.d(QuizActivity.TAG, "Saving average time: ${ans.averageTimeOfMenstruation}")
    }

}
