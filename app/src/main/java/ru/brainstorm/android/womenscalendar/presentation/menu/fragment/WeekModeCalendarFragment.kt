package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.DayViewContainer

class WeekModeCalendarFragment : Fragment() {




    var selectedDate: LocalDate = LocalDate.now()
    private var menstruationStartDate  = LocalDate.parse("2019-10-27")
    private var menstruationEndDate  = LocalDate.parse("2019-10-29")
    private var ovulationStartDate  = LocalDate.parse("2019-11-01")
    private var ovulationEndDate  = LocalDate.parse("2019-11-10")
    private var ovulationDate = LocalDate.parse("2019-11-07")




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week_mode_calendar, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //val monthText = view.MonthText
        wm.defaultDisplay.getMetrics(dm)

        calendarView.dayWidth = dm.widthPixels / 7

        calendarView.dayHeight = (calendarView.dayWidth * 1.25).toInt()



        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(
                view,
                this@WeekModeCalendarFragment.view!!,
                menstruationStartDate,
                menstruationEndDate,
                ovulationStartDate,
                ovulationEndDate,
                ovulationDate)
            override fun bind(container: DayViewContainer, day: CalendarDay) = container.bind(day)
        }

        val currentMonth = YearMonth.now()
        // Value for firstDayOfWeek does not matter since inDates and outDates are not generated.
        calendarView.setup(currentMonth, currentMonth.plusMonths(3), DayOfWeek.values().random())
        calendarView.scrollToDate(LocalDate.now())

        //changeCalendar(LocalDate.parse("2019-10-28"), LocalDate.parse("2019-10-30"), LocalDate.parse("2019-11-03"), LocalDate.parse("2019-11-09"))
    }


}
