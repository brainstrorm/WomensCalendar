package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import kotlinx.android.synthetic.main.calendar_day_week_mode.view.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.view.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.w3c.dom.Text
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.GetDayAddition
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.PartOfCycle
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.WeekModeCalendarPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.WeekModeCalendarView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.getColorCompat
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes

class WeekModeCalendarFragment : MvpAppCompatFragment(), WeekModeCalendarView {

    @InjectPresenter
    lateinit var weekModeCalendarPresenter: WeekModeCalendarPresenter

    private lateinit var TVScreen : ConstraintLayout
    private lateinit var TVIndicatorRound : ImageView
    private lateinit var TVIndicatorRing : ImageView
    private lateinit var TVPartOfCycle : TextView
    private lateinit var TVForecastText : TextView
    private lateinit var TVForecastNumberPred : TextView
    private lateinit var TVForecastNumberPost : TextView
    private lateinit var TVAdditionalInfo : TextView

    private var selectedDate: LocalDate? = LocalDate.now()
    private val today = LocalDate.now()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().weekModeCalendarPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week_mode_calendar, container, false)
        TVIndicatorRound = view.findViewById(R.id.indicatorRound)
        TVIndicatorRing = view.findViewById(R.id.indicatorRing)
        TVScreen = view.findViewById(R.id.screen)
        TVPartOfCycle = view.findViewById(R.id.TVPartOfCycle)
        TVForecastText = view.findViewById(R.id.TVForecastText)
        TVForecastNumberPred = view.findViewById(R.id.TVForecastNumberPred)
        TVForecastNumberPost = view.findViewById(R.id.TVForecastNumberPost)
        TVAdditionalInfo = view.findViewById(R.id.TVAdditionalInfo)
        return view
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


        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val monthText = view.exSevenMonthText
        wm.defaultDisplay.getMetrics(dm)

        calendarView.dayWidth = dm.widthPixels / 5

        calendarView.dayHeight = (calendarView.dayWidth * 1.25).toInt()

        class DayViewContainer(view: View) : ViewContainer(view) {
            val dayText = view.exSevenDayText
            val dateText = view.exSevenDateText
            val selectedView = view.exSevenSelectedView

            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    val firstDay = calendarView.findFirstVisibleDay()
                    val lastDay = calendarView.findLastVisibleDay()
                    if (firstDay == day) {
                        // If the first date on screen was clicked, we scroll to the date to ensure
                        // it is fully visible if it was partially off the screen when clicked.
                        calendarView.smoothScrollToDate(day.date)
                    } else if (lastDay == day) {
                        // If the last date was clicked, we scroll to 4 days ago, this forces the
                        // clicked date to be fully visible if it was partially off the screen.
                        // We scroll to 4 days ago because we show max of five days on the screen
                        // so scrolling to 4 days ago brings the clicked date into full visibility
                        // at the end of the calendar view.
                        calendarView.smoothScrollToDate(day.date.minusDays(4))
                    }

                    // Example: If you want the clicked date to always be centered on the screen,
                    // you would use: exSevenCalendar.smoothScrollToDate(day.date.minusDays(2))

                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        calendarView.notifyDateChanged(day.date)
                        oldDate?.let { calendarView.notifyDateChanged(it) }
                    }
                }
            }

            fun bind(day: CalendarDay) {
                this.day = day
                dateText.text = dateFormatter.format(day.date)
                dayText.text = dayFormatter.format(day.date)

                dateText.setTextColor(view.context.getColorCompat(if (day.date == selectedDate) R.color.color_red else R.color.color_White))
                if (day.date == selectedDate)
                    monthText.text = monthFormatter.format(day.date)
                selectedView.isVisible = day.date == selectedDate
            }
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) = container.bind(day)
        }

        val currentMonth = YearMonth.now()
        // Value for firstDayOfWeek does not matter since inDates and outDates are not generated.
        calendarView.setup(currentMonth, currentMonth.plusMonths(3), DayOfWeek.values().random())
        calendarView.scrollToDate(LocalDate.now())
    }


    override fun changeInformationInRound(
        indicator: PartOfCycle,
        forecast: String,
        numberOfDays: Int,
        additionalInfo: String
    ) {
        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION -> {
                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Менструация через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = GetDayAddition(numberOfDays)
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.EMPTY_OVULATION -> {
                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Овуляция через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = GetDayAddition(numberOfDays)
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_MENSTRUATION -> {
                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Менструация через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = GetDayAddition(numberOfDays)
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_OVULATION -> {
                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Овуляция через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = GetDayAddition(numberOfDays)
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.MENSTRUATION -> {
                TVForecastNumberPred.isVisible = false
                TVForecastNumberPost.isVisible = true

                TVPartOfCycle.text = "Менструация"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundPink)

                TVForecastText.text = "день"
                TVForecastText.setTextColorRes(R.color.colorForecastTextPink)

                TVForecastNumberPost.text = "$numberOfDays"
                TVForecastNumberPost.setTextColorRes(R.color.colorForecastNumberPink)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundPink)
            }
            PartOfCycle.OVULATION -> {
                TVForecastNumberPred.isVisible = false
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Прогноз: день"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)

                TVForecastText.text = "овуляции"
                TVForecastText.setTextColorRes(R.color.colorForecastTextYellow)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)
            }
            PartOfCycle.POST_OVULATION -> {
                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Менструация через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = GetDayAddition(numberOfDays)
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = additionalInfo
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
        }
    }

    override fun changePeriods(
        menstruationStart: Int,
        menstruationFinish: Int,
        ovulationStart: Int,
        ovulationFinish: Int
    ) {
    }

    override fun changeColors(indicator: PartOfCycle) {
        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION, PartOfCycle.EMPTY_OVULATION -> {
                TVScreen.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorEmpty))
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_empty)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_empty)
            }
            PartOfCycle.PRED_MENSTRUATION ->{
                TVScreen.setBackgroundResource(R.drawable.gradient_pred_menstruation)
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_pred_menstruation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_pred_menstruation)
            }
            PartOfCycle.MENSTRUATION  ->{
                TVScreen.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorMenstruation))
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_menstruation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_menstruation)
            }

            PartOfCycle.PRED_OVULATION -> {
                TVScreen.setBackgroundResource(R.drawable.gradient_pred_ovulation)
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_pred_ovulation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_pred_ovulation)
            }
            PartOfCycle.OVULATION -> {
                TVScreen.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorOvulation))
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_ovulation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_ovulation)
            }
            PartOfCycle.POST_OVULATION -> {
                TVScreen.setBackgroundResource(R.drawable.gradient_post_ovulation)
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_end_of_ovulation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_end_of_ovulation)
            }
        }

    }

}
