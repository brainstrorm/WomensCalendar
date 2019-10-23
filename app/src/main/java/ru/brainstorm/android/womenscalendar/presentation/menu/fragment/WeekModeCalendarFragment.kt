package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment

import ru.brainstorm.android.womenscalendar.R

class WeekModeCalendarFragment : MvpAppCompatFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_mode_calendar, container, false)
    }


    private var weekDays = HashMap<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekDays.put("Mon", "Пн")
        weekDays.put("Tue", "Вт")
        weekDays.put("Wed", "Ср")
        weekDays.put("Thu", "Чт")
        weekDays.put("Fri", "Пт")
        weekDays.put("Sat", "Сб")
        weekDays.put("Sun", "Вс")


    }

}
