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
import ru.brainstorm.android.womenscalendar.data.User
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import java.time.LocalDate
import java.util.*


class BirthDateFragment : AbstractQuizFragment() {

    private lateinit var choose: TextView
    private lateinit var averageMenstruationPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_birth_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        averageMenstruationPicker = view.findViewById(R.id.birthDatePicker)
        choose = view.findViewById(R.id.choose)
        averageMenstruationPicker.minValue = 1900
        averageMenstruationPicker.maxValue = 2019
        averageMenstruationPicker.value = 2000
        averageMenstruationPicker.setDividerColorResource(android.R.color.transparent)
        averageMenstruationPicker.setTextColorResource(R.color.colorGreyFont)
        averageMenstruationPicker.setTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
        averageMenstruationPicker.setSelectedTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setOnValueChangedListener { _, _, _ ->
            choose.isVisible = false
        }
    }
  
    override fun getStep(): Int = 3

    override fun getNextFragment(): AbstractQuizFragment? {
        return null
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return AverageCycleFragment()
    }

    override fun setQuizAns(cycle: Cycle) {
        val birthDate = Date()
        val c = Calendar.getInstance()
        c.time = birthDate
        c.set(Calendar.DAY_OF_YEAR, 1)
        c.set(Calendar.MONTH, 1)
        c.set(Calendar.YEAR, averageMenstruationPicker.value)
        User.birthDate = LocalDate.of(averageMenstruationPicker.value, 1, 1).toString()
        Log.d(QuizActivity.TAG, "Saving date: ${User.birthDate}")
    }

}
