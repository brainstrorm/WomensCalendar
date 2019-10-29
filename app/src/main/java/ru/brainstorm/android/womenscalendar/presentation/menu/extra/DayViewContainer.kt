package ru.brainstorm.android.womenscalendar.presentation.menu.extra

import android.view.View
import androidx.core.view.isVisible
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day_week_mode.view.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.getColorCompat
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes

class DayViewContainer(
    view: View,
    weekModeFragment : View,
    var menstruationStartDate : LocalDate,
    var menstruationEndDate : LocalDate, var ovulationStartDate : LocalDate,
    var ovulationEndDate : LocalDate,
    var ovulationDate : LocalDate) : ViewContainer(view) {

    var selectedDate: LocalDate = LocalDate.now()
    private var dayText = view.exSevenDayText
    private var dateText = view.exSevenDateText
    private var selectedView = view.exSevenSelectedView
    private var monthText = weekModeFragment.MonthText
    private var TVScreen = weekModeFragment.screen
    private var TVIndicatorRound = weekModeFragment.indicatorRound
    private var TVIndicatorRing = weekModeFragment.indicatorRing
    private var TVPartOfCycle = weekModeFragment.TVPartOfCycle
    private var TVForecastText = weekModeFragment.TVForecastText
    private var TVForecastNumberPred = weekModeFragment.TVForecastNumberPred
    private var TVForecastNumberPost = weekModeFragment.TVForecastNumberPost
    private var TVAdditionalInfo = weekModeFragment.TVAdditionalInfo
    private var TVToday = weekModeFragment.TVToday
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val dayFormatterRound = DateTimeFormatter.ofPattern("EEEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val months = hashMapOf(
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

    lateinit var day: CalendarDay

    init {
        view.setOnClickListener {
            val firstDay = weekModeFragment.calendarView.findFirstVisibleDay()
            val lastDay = weekModeFragment.calendarView.findLastVisibleDay()
            if (firstDay == day) {
                // If the first date on screen was clicked, we scroll to the date to ensure
                // it is fully visible if it was partially off the screen when clicked.
                weekModeFragment.calendarView.smoothScrollToDate(day.date)
            } else if (lastDay == day) {
                // If the last date was clicked, we scroll to 4 days ago, this forces the
                // clicked date to be fully visible if it was partially off the screen.
                // We scroll to 4 days ago because we show max of five days on the screen
                // so scrolling to 4 days ago brings the clicked date into full visibility
                // at the end of the calendar view.
                weekModeFragment.calendarView.smoothScrollToDate(day.date.minusDays(4))
            }

            // Example: If you want the clicked date to always be centered on the screen,
            // you would use: exSevenCalendar.smoothScrollToDate(day.date.minusDays(2))

            if (selectedDate != day.date) {
                val oldDate = selectedDate
                selectedDate = day.date
                weekModeFragment.calendarView.notifyDateChanged(day.date)
                oldDate?.let { weekModeFragment.calendarView.notifyDateChanged(it) }
            }
        }
    }
    fun bind(day: CalendarDay) {
        this.day = day
        dateText.text = dateFormatter.format(day.date)
        dayText.text = dayFormatter.format(day.date)
        if (day.date == selectedDate)
            monthText.text = months[monthFormatter.format(day.date)]!!.capitalize()
        when(day.date){
            selectedDate -> {
                dateText.setTextColor(view.context.getColorCompat(R.color.color_White))
                if(selectedDate < menstruationStartDate){
                    selectedView.setBackgroundResource(R.drawable.week_mode_single_selected_day)
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
                        selectedView.setBackgroundResource(R.drawable.blob_field_selected)
                        changeInformationInRound(PartOfCycle.MENSTRUATION,
                            selectedDate.dayOfYear - menstruationStartDate.dayOfYear + 1,
                            day
                        )
                        changeColors(PartOfCycle.MENSTRUATION)
                    }
                    in ovulationStartDate..ovulationEndDate-> {
                        if(selectedDate < ovulationDate) {
                            selectedView.setBackgroundResource(R.drawable.orange_field_selected)
                            changeInformationInRound(
                                PartOfCycle.PRED_OVULATION,
                                ovulationDate.dayOfYear - selectedDate.dayOfYear,
                                day
                            )
                            changeColors(PartOfCycle.PRED_OVULATION)
                        }
                        if (selectedDate == ovulationDate){
                            selectedView.setBackgroundResource(R.drawable.ovulation_round_selected)
                            changeInformationInRound(
                                PartOfCycle.OVULATION,
                                0,
                                day
                            )
                            changeColors(PartOfCycle.OVULATION)
                        }
                        if ((selectedDate > ovulationDate) && (selectedDate <= ovulationEndDate)){
                            selectedView.setBackgroundResource(R.drawable.orange_field_selected)
                            changeInformationInRound(
                                PartOfCycle.POST_OVULATION,
                                menstruationStartDate.dayOfYear - selectedDate.dayOfYear,
                                day
                            )
                            changeColors(PartOfCycle.POST_OVULATION)
                        }
                    }
                    in ovulationEndDate..menstruationStartDate -> {
                        selectedView.setBackgroundResource(R.drawable.week_mode_single_selected_day)
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
                        selectedView.setBackgroundResource(R.drawable.week_mode_single_selected_day)
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
            ovulationDate -> {
                selectedView.setBackgroundResource(R.drawable.ovulation_round_not_selected)
                dateText.setTextColor(view.context.getColorCompat(R.color.colorOfChosenNumberOrange))
            }
            in ovulationStartDate..ovulationEndDate -> dateText.setTextColor(view.context.getColorCompat(R.color.colorOfChosenNumberOrange))
            else -> dateText.setTextColor(view.context.getColorCompat(R.color.colorDays))
        }
        selectedView.isVisible = (day.date == selectedDate) || (day.date == ovulationDate)
    }


    fun setColorsOfBackgroundAndVectors(backgroundColor : Int, roundColor : Int, ringColor : Int ){
        TVScreen.setBackgroundResource(backgroundColor)
        TVIndicatorRound.setBackgroundResource(roundColor)
        TVIndicatorRing.setBackgroundResource(ringColor)
    }
    fun setTextViews(
        day: CalendarDay,
        numberOfDays: Int,
        predNumber : Boolean,
        postNumber : Boolean,
        partOfCycleTextId : Int,
        partOfCycleColor : Int,
        forecastNumberColor : Int,
        forecastTextColor : Int,
        additionalInfoTextId : Int,
        additionalInfoColor: Int
    ) {
        TVToday.text =
            "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(
                day.date
            )}"

        TVForecastNumberPred.isVisible = predNumber
        TVForecastNumberPost.isVisible = postNumber

        TVPartOfCycle.setText(partOfCycleTextId)
        TVPartOfCycle.setTextColorRes(partOfCycleColor)

        TVForecastNumberPred.text = "$numberOfDays"
        TVForecastNumberPred.setTextColorRes(forecastNumberColor)

        TVForecastText.text = numberOfDays.getDayAddition()
        TVForecastText.setTextColorRes(forecastTextColor)

        TVAdditionalInfo.setText(additionalInfoTextId)
        TVAdditionalInfo.setTextColorRes(additionalInfoColor)
    }

    fun changeInformationInRound(
        indicator: PartOfCycle,
        numberOfDays: Int,
        day : CalendarDay
    ) {
        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION -> {
                setTextViews(
                    day,
                    numberOfDays,
                    true,
                    false,
                    R.string.menstruation_after,
                    R.color.colorPartOfCycleInRoundBlue,
                    R.color.colorForecastNumberBlue,
                    R.color.colorForecastTextBlue,
                    R.string.unlikely_to_get_pregnant,
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.EMPTY_OVULATION -> {
                setTextViews(
                    day,
                    numberOfDays,
                    true,
                    false,
                    R.string.ovulation_after,
                    R.color.colorPartOfCycleInRoundBlue,
                    R.color.colorForecastNumberBlue,
                    R.color.colorForecastTextBlue,
                    R.string.unlikely_to_get_pregnant,
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_MENSTRUATION -> {
                setTextViews(
                    day,
                    numberOfDays,
                    true,
                    false,
                    R.string.menstruation_after,
                    R.color.colorPartOfCycleInRoundBlue,
                    R.color.colorForecastNumberBlue,
                    R.color.colorForecastTextBlue,
                    R.string.unlikely_to_get_pregnant,
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_OVULATION -> {
                setTextViews(
                    day,
                    numberOfDays,
                    true,
                    false,
                    R.string.ovulation_after,
                    R.color.colorPartOfCycleInRoundBlue,
                    R.color.colorForecastNumberBlue,
                    R.color.colorForecastTextBlue,
                    R.string.average_probability_of_getting_pregnant,
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.MENSTRUATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = false
                TVForecastNumberPost.isVisible = true

                TVPartOfCycle.setText(R.string.menstruation)
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundPink)

                TVForecastText.setText(R.string.day)
                TVForecastText.setTextColorRes(R.color.colorForecastTextPink)

                TVForecastNumberPost.text = "$numberOfDays"
                TVForecastNumberPost.setTextColorRes(R.color.colorForecastNumberPink)

                TVAdditionalInfo.text = "${view.context!!.getText(R.string.the_cycle_lasted)} \n 30 ${30.getDayAddition()}"
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundPink)
            }
            PartOfCycle.OVULATION -> {
                TVToday.text = "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(day.date)}"

                TVForecastNumberPred.isVisible = false
                TVForecastNumberPost.isVisible = false

                TVPartOfCycle.setText(R.string.forecast_day)
                TVPartOfCycle.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)

                TVForecastText.setText(R.string.ovulation)
                TVForecastText.setTextColorRes(R.color.colorForecastTextYellow)

                TVAdditionalInfo.setText(R.string.high_probability_of_getting_pregnant)
                TVAdditionalInfo.setTextColorRes(R.color.colorPartOfCycleInRoundYellow)
            }
            PartOfCycle.POST_OVULATION -> {
                setTextViews(
                    day,
                    numberOfDays,
                    true,
                    false,
                    R.string.menstruation_after,
                    R.color.colorPartOfCycleInRoundYellow,
                    R.color.colorForecastNumberBlue,
                    R.color.colorForecastTextBlue,
                    R.string.high_probability_of_getting_pregnant,
                    R.color.colorPartOfCycleInRoundBlue)
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
