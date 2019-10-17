package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.shawnlin.numberpicker.NumberPicker
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.AverageCyclePresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.AverageCycleView


class AverageCycleFragment : AbstractQuizFragment(), AverageCycleView {

    @InjectPresenter
    lateinit var fragmentPresenter: AverageCyclePresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().averageCyclePresenter()

    private lateinit var choose: TextView
    private lateinit var days: TextView
    private lateinit var averageMenstruationPicker: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_average_cycle, container, false)

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
        averageMenstruationPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            choose.isVisible = false
            days.isVisible = true
        }
        return view
    }

    override fun getStep(): Int = 2

    override fun getNextFragment(): AbstractQuizFragment? {
        return BirthDateFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return AverageMenstruationFragment()
    }

    override fun setQuizAns(ans: QuizAnswers) {
        ans.averageTimeOfCycle = averageMenstruationPicker.value
        Log.d(QuizActivity.TAG, "Saving cycle length: ${ans.averageTimeOfCycle}")
    }
}
