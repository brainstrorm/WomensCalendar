package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

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
import kotlinx.android.synthetic.main.calendar_day_legend.view.*
import kotlinx.android.synthetic.main.calendar_header.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.ChangeMenstruationDatesPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ChangeMenstruationDatesView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.daysOfWeekFromLocale
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.makeInVisible
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.makeVisible
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
import kotlin.math.round


class ChangeMenstruationDatesFragment : AbstractMenuFragment(), ChangeMenstruationDatesView {

    @InjectPresenter
    lateinit var changeMenstruationDatesPresenter: ChangeMenstruationDatesPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().changeMenstruationDatesPresenter()

    companion object{
        val TAG = "ChangeMenstruationDates"
    }

    private var startDate : LocalDate? = null
    private var endDate : LocalDate? = null

    override fun getPart(): String = "change_menstruation_dates"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        App.appComponent.inject(this)
        return inflater.inflate(R.layout.fragment_change_menstruation_dates, container, false)
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


        val currentMonth = YearMonth.now()
        calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = view.findViewById<TextView>(R.id.calendarDayText)
            val roundField = view.findViewById<View>(R.id.exFourRoundBgView)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        val date = day.date
                        if (startDate != null) {
                            if (date < startDate || endDate != null) {
                                startDate = date
                                endDate = null
                            } else if (date != startDate) {
                                endDate = date
                            }
                        } else {
                            startDate = date
                        }
                        calendarView.notifyCalendarChanged()
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
                val roundField = container.roundField

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.date.dayOfMonth.toString()
                    when{
                        startDate == day.date && endDate == null -> {
                            textView.setTextColorRes(R.color.colorPrimaryDark)
                            roundField.makeVisible()
                            roundField.setBackgroundResource(R.drawable.round_field_selected)
                        }
                        day.date == startDate -> {
                            textView.setTextColorRes(R.color.colorPrimaryDark)
                            roundField.setBackgroundResource(R.drawable.round_field_selected)
                        }
                        startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                            textView.setTextColorRes(R.color.colorPrimaryDark)
                            roundField.setBackgroundResource(R.drawable.round_field_selected)
                        }
                        day.date == endDate -> {
                            textView.setTextColorRes(R.color.colorPrimaryDark)
                            roundField.setBackgroundResource(R.drawable.round_field_selected)
                        }
                        day.date == today -> {
                            textView.setTextColorRes(R.color.color_red)
                            roundField.setBackgroundResource(R.drawable.round_field_not_selected)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.colorDays)
                            roundField.setBackgroundResource(R.drawable.round_field_not_selected)
                        }
                    }
                } else {
                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.
                    roundField.makeInVisible()
                    val startDate = startDate
                    val endDate = endDate
                    if (startDate != null && endDate != null) {
                        // Mimic selection of inDates that are less than the startDate.
                        // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                        // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                        if ((day.owner == DayOwner.PREVIOUS_MONTH
                                    && startDate.monthValue == day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue) ||
                            // Mimic selection of outDates that are greater than the endDate.
                            // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                            // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                            (day.owner == DayOwner.NEXT_MONTH
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue == day.date.monthValue) ||

                            // Mimic selection of in and out dates of intermediate
                            // months if the selection spans across multiple months.
                            (startDate < day.date && endDate > day.date
                                    && startDate.monthValue != day.date.monthValue
                                    && endDate.monthValue != day.date.monthValue)
                        ) {
                            roundField.setBackgroundResource(R.drawable.round_field_selected)
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
            val legendLayout = view.legendLayout.children.forEachIndexed { index, view ->
                (view as TextView).apply {
                    text = weekDays[daysOfWeek[index].name.take(3).toLowerCase().capitalize()]!!
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    setTextColorRes(R.color.colorDaysOfWeek)
                }
            }
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                container.textView.text = "${months[month.yearMonth.month.name.toLowerCase().capitalize()]!!} ${month.year}"
            }
        }
    }

    fun getStartDate() : LocalDate?{
        return startDate
    }

    fun getEndDate() : LocalDate? {
        return endDate
    }

}