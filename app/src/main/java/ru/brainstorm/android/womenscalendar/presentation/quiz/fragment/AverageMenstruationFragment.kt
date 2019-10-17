package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.R.color
import android.os.Bundle
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
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.AverageMenstruationPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.AverageMenstruationView


class AverageMenstruationFragment : AbstractQuizFragment(), AverageMenstruationView{

    @InjectPresenter
    lateinit var fragmentPresenter: AverageMenstruationPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().averageMenstruationPresenter()

    private lateinit var choose: TextView
    private lateinit var days: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_average_menstruation, container, false)

        val averageMenstruationPicker = view.findViewById<NumberPicker>(R.id.averageMenstruationPicker)
        choose = view.findViewById(R.id.choose)
        days = view.findViewById(R.id.days)
        averageMenstruationPicker.minValue = 0
        averageMenstruationPicker.maxValue = 10
        averageMenstruationPicker.setDividerColorResource(color.transparent)
//        averageMenstruationPicker.setTextColorResource(R.color.colorGreyFont)
//        averageMenstruationPicker.setTextSize(R.dimen.text_size_picker)
//        averageMenstruationPicker.setSelectedTextColorResource(R.color.colorOfChosenNumber)
//        averageMenstruationPicker.setSelectedTextSize(R.dimen.text_size_picker)
        averageMenstruationPicker.setOnValueChangedListener { _, _, _ ->
            choose.isVisible = false
            days.isVisible = true
        }
        return view
    }

    override fun getStep(): Int = 2

    override fun getNextFragment(): AbstractQuizFragment? {
        return null
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return null
    }
}
