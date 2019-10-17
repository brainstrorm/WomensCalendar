package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.shawnlin.numberpicker.NumberPicker
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.AverageCyclePresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.AverageCycleView


class AverageCycleFragment : AbstractQuizFragment(), AverageCycleView {

    @InjectPresenter
    lateinit var fragmentPresenter: AverageCyclePresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().averageCyclePresenter()

    private lateinit var choose: TextView
    private lateinit var days: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_average_cycle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val averageMenstruationPicker = view.findViewById<NumberPicker>(R.id.averageCyclePicker)
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

    override fun getStep(): Int = 3

    override fun getNextFragment(): AbstractQuizFragment? {
        return BirthDateFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return AverageMenstruationFragment()
    }
}
