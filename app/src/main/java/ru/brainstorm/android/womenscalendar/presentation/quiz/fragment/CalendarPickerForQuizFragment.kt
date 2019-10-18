package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.calendar_header.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.CalendarPickerView_

class CalendarPickerForQuizFragment : CalendarPickerView_, AbstractQuizFragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_calendar_picker_for_quiz, container, false)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private var weekDays = HashMap<String, String>()
    private var months = HashMap<String, String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        weekDays.put("Mon", "Пн")
        weekDays.put("Tue", "Вт")
        weekDays.put("Wed", "Ср")
        weekDays.put("Thu", "Чт")
        weekDays.put("Fri", "Пт")
        weekDays.put("Sat", "Сб")
        weekDays.put("Sun", "Вс")

        months.put("October", "Октябрь")
        months.put("November", "Ноябрь")
        months.put("December", "Декабрь")
        months.put("January", "Январь")
        months.put("February", "Февраль")
        months.put("March", "Март")
        months.put("April", "Апрель")
        months.put("May", "Май")
        months.put("June", "Июнь")
        months.put("July", "Июль")
        months.put("August", "Август")
        months.put("September", "Сентябрь")

        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = weekDays[daysOfWeek[index].name.take(3).toLowerCase().capitalize()]!!
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTextColorRes(R.color.example_4_grey)
            }
        }


        val currentMonth = YearMonth.now()
        calendarView.setup(currentMonth, currentMonth.plusMonths(12), daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = view.findViewById<TextView>(R.id.calendarDayText)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            calendarView.notifyDateChanged(day.date)
                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
                        }
                        //menuItem.isVisible = selectedDate != null
                    }
                }
            }

        }
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        selectedDate -> {
                            textView.setTextColorRes(R.color.color_White)
                            textView.setBackgroundResource(R.drawable.example_4_single_selected_bg)
                        }
                        today -> {
                            textView.setTextColorRes(R.color.color_red)
                            textView.background = null
                        }
                        else -> {
                            textView.setTextColorRes(R.color.design_default_color_primary)
                            textView.background = null
                        }
                    }
                } else {
                    textView.makeInVisible()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                container.textView.text = "${months[month.yearMonth.month.name.toLowerCase().capitalize()]!!} ${month.year}"
            }
        }
    }

    /*private lateinit var menuItem: MenuItem
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.example_2_menu, menu)
        menuItem = menu.getItem(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuItemDone) {
            val date = selectedDate ?: return false
            val text = "Selected: ${DateTimeFormatter.ofPattern("d MMMM yyyy").format(date)}"
            Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
            fragmentManager?.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun getStep(): Int = 1

    override fun getNextFragment(): AbstractQuizFragment? {
        return AverageMenstruationFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return null
    }
}
