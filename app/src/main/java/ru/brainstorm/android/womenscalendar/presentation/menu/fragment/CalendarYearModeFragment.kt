package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.telerik.widget.calendar.CalendarDisplayMode


import com.telerik.widget.calendar.RadCalendarView
import java.util.*


class CalendarYearModeFragment : AbstractMenuFragment(){
    override fun getPart(): String {
        return "calendar_year_mode"
    }

    companion object{
        val TAG = "CalendarYearMode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(ru.brainstorm.android.womenscalendar.R.layout.fragment_calendar_year_mode, container, false)
        val calendarView = view.findViewById(ru.brainstorm.android.womenscalendar.R.id.calendarView) as RadCalendarView
        calendarView.displayMode = CalendarDisplayMode.Year
        //PopupWindow.OnDismissListener {  }
        //val calendar = GregorianCalendar(2014, Calendar.JANUARY, 1)
        //calendarView.displayDate = calendar.getTimeInMillis()
        return inflater.inflate(ru.brainstorm.android.womenscalendar.R.layout.fragment_calendar_year_mode, container, false)

    }
}
