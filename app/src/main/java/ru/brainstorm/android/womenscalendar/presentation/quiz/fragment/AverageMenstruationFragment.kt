package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_average_menstruation.*
import moxy.MvpAppCompatFragment

import ru.brainstorm.android.womenscalendar.R
import android.R.color
import java.lang.reflect.AccessibleObject.setAccessible
import android.graphics.Paint
import android.widget.TextView
import androidx.core.view.isVisible
import com.shawnlin.numberpicker.NumberPicker


class AverageMenstruationFragment : MvpAppCompatFragment() {

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

        val view = inflater.inflate(R.layout.fragment_average_menstruation, container, false)

        val averageMenstruationPicker = view.findViewById<NumberPicker>(R.id.averageMenstruationPicker)
        choose = view.findViewById(R.id.choose)
        days = view.findViewById(R.id.days)
        averageMenstruationPicker.minValue = 0
        averageMenstruationPicker.maxValue = 10
        averageMenstruationPicker.setDividerColorResource(color.transparent)
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

}
