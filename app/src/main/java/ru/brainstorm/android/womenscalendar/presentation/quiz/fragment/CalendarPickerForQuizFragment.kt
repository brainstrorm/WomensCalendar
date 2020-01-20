package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
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
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.CalendarPickerForQuizPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.CalendarPickerForQuizView
import java.util.*
import kotlin.collections.HashMap

class CalendarPickerForQuizFragment : AbstractQuizFragment(), CalendarPickerForQuizView{

    private lateinit var pref : SharedPreferences

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

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        var calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        updateLocale()

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

    override fun setQuizAns(cycle: Cycle) {
        selectedDate ?: return
        cycle.startOfCycle = selectedDate!!.toString()
    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        months.put("October", resources.getString(R.string.october))
        months.put("November", resources.getString(R.string.november))
        months.put("December", resources.getString(R.string.december))
        months.put("January", resources.getString(R.string.january))
        months.put("February", resources.getString(R.string.february))
        months.put("March", resources.getString(R.string.march))
        months.put("April", resources.getString(R.string.april))
        months.put("May", resources.getString(R.string.may))
        months.put("June", resources.getString(R.string.june))
        months.put("July", resources.getString(R.string.july))
        months.put("August", resources.getString(R.string.august))
        months.put("September", resources.getString(R.string.september))

        weekDays.put("Mon", resources.getString(R.string.monday))
        weekDays.put("Tue", resources.getString(R.string.tuesday))
        weekDays.put("Wed", resources.getString(R.string.wednesday))
        weekDays.put("Thu", resources.getString(R.string.thursday))
        weekDays.put("Fri", resources.getString(R.string.friday))
        weekDays.put("Sat", resources.getString(R.string.saturday))
        weekDays.put("Sun", resources.getString(R.string.sunday))
    }
}
