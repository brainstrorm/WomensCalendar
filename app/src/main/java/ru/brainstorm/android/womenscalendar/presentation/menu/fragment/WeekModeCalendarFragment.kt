package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day_week_mode.view.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.view.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.getDayAddition
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.PartOfCycle
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.getColorCompat
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes

class WeekModeCalendarFragment : Fragment() {



    private lateinit var TVScreen : ConstraintLayout
    private lateinit var TVIndicatorRound : ImageView
    private lateinit var TVIndicatorRing : ImageView
    private lateinit var TVPartOfCycle : TextView
    private lateinit var TVForecastText : TextView
    private lateinit var TVForecastNumberPred : TextView
    private lateinit var TVForecastNumberPost : TextView
    private lateinit var TVAdditionalInfo : TextView
    private lateinit var TVToday : TextView

    private var selectedDate: LocalDate = LocalDate.now()
    private val today = LocalDate.now()
    private var menstruationStartDate  = LocalDate.parse("2019-10-27")
    private var menstruationEndDate  = LocalDate.parse("2019-10-29")
    private var ovulationStartDate  = LocalDate.parse("2019-11-01")
    private var ovulationEndDate  = LocalDate.parse("2019-11-10")
    private var ovulationDate = LocalDate.parse("2019-11-07")

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val dayFormatterRound = DateTimeFormatter.ofPattern("EEEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")

    private var months = hashMapOf(
        "января" to "январь",
        "февраля" to "февраль",
        "марта" to "март",
        "апреля" to "апрель",
        "мая" to "май",
        "июня" to "июнь",
        "июля" to "июль",
        "августа" to "август",
        "сентября" to "сентябрь",
        "октября" to "октябрь",
        "ноября" to "ноябрь",
        "декабря" to "декабрь"
    )

    private fun setColorsOfBackgroundAndVectors(backgroundColor : Int, roundColor : Int, ringColor : Int ){
        TVScreen.setBackgroundResource(backgroundColor)
        TVIndicatorRound.setBackgroundResource(roundColor)
        TVIndicatorRing.setBackgroundResource(ringColor)
    }

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
        TVToday = view.findViewById(R.id.TVToday)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val monthText = view.MonthText
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
//
            fun bind(day: CalendarDay) {
                this.day = day
                dateText.text = dateFormatter.format(day.date)
                dayText.text = dayFormatter.format(day.date)
                if (day.date == selectedDate)
                    monthText.text = months[monthFormatter.format(day.date)]!!.capitalize()
                //dateText.setTextColor(view.context.getColorCompat(if (day.date == selectedDate) R.color.color_White else R.color.colorDays))
                when(day.date){
                    selectedDate -> {
                        dateText.setTextColor(view.context.getColorCompat(R.color.color_White))
                        if(selectedDate < menstruationStartDate){
                            if(menstruationStartDate.dayOfYear - selectedDate.dayOfYear > 5){
                                changeInformationInRound(
                                    PartOfCycle.EMPTY_MENSTRUATION,
                                    menstruationStartDate.dayOfYear - selectedDate.dayOfYear,
                                    day
                                )
                                changeColors(PartOfCycle.EMPTY_MENSTRUATION)
                            }else{
                                changeInformationInRound(
                                    PartOfCycle.PRED_MENSTRUATION,
                                    menstruationStartDate.dayOfYear - selectedDate.dayOfYear,
                                    day
                                )
                                changeColors(PartOfCycle.PRED_MENSTRUATION)
                            }
                        }
                        when(selectedDate){
                            in menstruationStartDate..menstruationEndDate -> {
                                changeInformationInRound(PartOfCycle.MENSTRUATION,
                                    selectedDate.dayOfYear - menstruationStartDate.dayOfYear + 1,
                                    day
                                )
                                changeColors(PartOfCycle.MENSTRUATION)
                            }
                            in ovulationStartDate..ovulationDate-> {
                                if(selectedDate < ovulationDate) {
                                    changeInformationInRound(
                                        PartOfCycle.PRED_OVULATION,
                                        ovulationDate.dayOfYear - selectedDate.dayOfYear,
                                        day
                                    )
                                    changeColors(PartOfCycle.PRED_OVULATION)
                                }
                                if (selectedDate == ovulationDate){
                                    changeInformationInRound(
                                        PartOfCycle.OVULATION,
                                        0,
                                        day
                                    )
                                    changeColors(PartOfCycle.OVULATION)
                                }
                                if (selectedDate > ovulationDate){
                                    changeInformationInRound(
                                        PartOfCycle.POST_OVULATION,
                                        menstruationStartDate.dayOfYear - selectedDate.dayOfYear,
                                        day
                                    )
                                    changeColors(PartOfCycle.POST_OVULATION)
                                }
                            }
                            in ovulationEndDate..menstruationStartDate -> {
                                if(menstruationStartDate.dayOfYear - selectedDate.dayOfYear > 5){
                                    changeInformationInRound(
                                        PartOfCycle.EMPTY_MENSTRUATION,
                                        menstruationStartDate.dayOfYear - selectedDate.dayOfYear,
                                        day
                                    )
                                    changeColors(PartOfCycle.EMPTY_MENSTRUATION)
                                }else{
                                    changeInformationInRound(
                                        PartOfCycle.PRED_MENSTRUATION,
                                        menstruationStartDate.dayOfYear - selectedDate.dayOfYear,
                                        day
                                    )
                                    changeColors(PartOfCycle.PRED_MENSTRUATION)
                                }
                            }
                            in menstruationEndDate..ovulationStartDate -> {
                                changeInformationInRound(
                                    PartOfCycle.EMPTY_OVULATION,
                                    ovulationDate.dayOfYear - selectedDate.dayOfYear,
                                    day
                                )
                                changeColors(PartOfCycle.EMPTY_OVULATION)
                            }
                        }
                    }
                    in menstruationStartDate..menstruationEndDate -> dateText.setTextColor(view.context.getColorCompat(R.color.colorOfChosenNumber))
                    in ovulationStartDate..ovulationEndDate -> dateText.setTextColor(view.context.getColorCompat(R.color.colorOfChosenNumberOrange))
                    else -> dateText.setTextColor(view.context.getColorCompat(R.color.colorDays))
                }
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

        //changeCalendar(LocalDate.parse("2019-10-28"), LocalDate.parse("2019-10-30"), LocalDate.parse("2019-11-03"), LocalDate.parse("2019-11-09"))
    }


    fun changeInformationInRound(
        indicator: PartOfCycle,
        numberOfDays: Int,
        day : CalendarDay
    ) {
        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Менструация через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = numberOfDays.getDayAddition()
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = "Малая вероятность \n забеременить"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.EMPTY_OVULATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Овуляция через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = numberOfDays.getDayAddition()
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = "Малая вероятность \n забеременить"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_MENSTRUATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Менструация через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = numberOfDays.getDayAddition()
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = "Малая вероятность \n забеременить"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_OVULATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Овуляция через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = numberOfDays.getDayAddition()
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = "Средняя вероятность \n забеременить"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.MENSTRUATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = false
                TVForecastNumberPost.isVisible = true

                TVPartOfCycle.text = "Менструация"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundPink)

                TVForecastText.text = "день"
                TVForecastText.setTextColorRes(R.color.colorForecastTextPink)

                TVForecastNumberPost.text = "$numberOfDays"
                TVForecastNumberPost.setTextColorRes(R.color.colorForecastNumberPink)

                TVAdditionalInfo.text = "Цикл длился \n 30 дней"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundPink)
            }
            PartOfCycle.OVULATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = false
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Прогноз: день"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)

                TVForecastText.text = "овуляции"
                TVForecastText.setTextColorRes(R.color.colorForecastTextYellow)

                TVAdditionalInfo.text = "Высокая вероятность \n забеременить"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)
            }
            PartOfCycle.POST_OVULATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = true
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.text = "Менструация через"
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)

                TVForecastNumberPred.text = "$numberOfDays"
                TVForecastNumberPred.setTextColorRes(R.color.colorForecastNumberBlue)

                TVForecastText.text = numberOfDays.getDayAddition()
                TVForecastText.setTextColorRes(R.color.colorForecastTextBlue)

                TVAdditionalInfo.text = "Высокая вероятность \n забеременить"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundBlue)
            }
        }
    }

    fun changeCalendar(
        menstruationStart: LocalDate,
        menstruationFinish: LocalDate,
        ovulationStart:     LocalDate,
        ovulationFinish: LocalDate
    ) {
        menstruationStartDate = menstruationStart
        menstruationEndDate = menstruationFinish
        ovulationStartDate = ovulationStart
        ovulationEndDate = ovulationFinish
    }

    fun changeColors(indicator: PartOfCycle) {
        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION, PartOfCycle.EMPTY_OVULATION -> {
                setColorsOfBackgroundAndVectors(R.color.colorEmpty, R.drawable.indicator_round_empty, R.drawable.indicator_ring_empty)
            }
            PartOfCycle.PRED_MENSTRUATION ->{
                setColorsOfBackgroundAndVectors(R.drawable.gradient_pred_menstruation, R.drawable.indicator_round_pred_menstruation, R.drawable.indicator_ring_pred_menstruation)
            }
            PartOfCycle.MENSTRUATION  ->{
                setColorsOfBackgroundAndVectors(R.color.colorMenstruation, R.drawable.indicator_round_menstruation, R.drawable.indicator_ring_menstruation)
            }

            PartOfCycle.PRED_OVULATION -> {
                setColorsOfBackgroundAndVectors(R.drawable.gradient_pred_ovulation, R.drawable.indicator_round_pred_ovulation, R.drawable.indicator_ring_pred_ovulation)
            }
            PartOfCycle.OVULATION -> {
                setColorsOfBackgroundAndVectors(R.color.colorOvulation, R.drawable.indicator_round_ovulation, R.drawable.indicator_ring_ovulation)
            }
            PartOfCycle.POST_OVULATION -> {
                setColorsOfBackgroundAndVectors(R.drawable.gradient_post_ovulation, R.drawable.indicator_round_end_of_ovulation, R.drawable.indicator_ring_end_of_ovulation)
            }
        }

    }

}
