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
import kotlinx.android.synthetic.main.calendar_day_legend.view.*
import kotlinx.android.synthetic.main.calendar_header.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.CalendarPickerForQuizPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.CalendarPickerForQuizView

class CalendarPickerForQuizFragment : AbstractQuizFragment(), CalendarPickerForQuizView{

    @InjectPresenter
    lateinit var fragmentPresenter: CalendarPickerForQuizPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().calendarPickerForQuizPresenter()

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
                val roundField = container.roundField
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    roundField.makeVisible()
                    when (day.date) {
                        selectedDate -> {
                            roundField.setBackgroundResource(R.drawable.round_field_selected)
                        }
                        today -> {
                            textView.setTextColorRes(R.color.color_red)
                            roundField.setBackgroundResource(R.drawable.round_field_not_selected)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.colorDays)
                            roundField.setBackgroundResource(R.drawable.round_field_not_selected)
                        }
                    }
                } else {
                    textView.makeInVisible()
                    roundField.makeInVisible()
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

    override fun getStep(): Int = 0

    override fun getNextFragment(): AbstractQuizFragment? {
        return AverageMenstruationFragment()
    }

    override fun getPrevFragment(): AbstractQuizFragment? {
        return null
    }

    override fun setQuizAns(ans: QuizAnswers) {

    }
}
