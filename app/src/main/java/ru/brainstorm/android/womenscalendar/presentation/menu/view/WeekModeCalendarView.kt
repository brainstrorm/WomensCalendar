package ru.brainstorm.android.womenscalendar.presentation.menu.view


import com.kizitonwose.calendarview.model.CalendarDay
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.PartOfCycle
import java.util.*

interface WeekModeCalendarView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeInformationInRound(
        indicator: PartOfCycle,
        numberOfDays : Int,
        day : CalendarDay
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeCalendar(
        menstruationStart : LocalDate,
        menstruationFinish : LocalDate,
        ovulationStart : LocalDate,
        ovulationFinish : LocalDate
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeColors(indicator : PartOfCycle)
}