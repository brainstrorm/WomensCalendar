package ru.brainstorm.android.womenscalendar.presentation.menu.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.PartOfCycle

interface WeekModeCalendarView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeInformationInRound(
        partOfCycle: String,
        forecast : String,
        additionalInfo : String
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changePeriods(
        menstruationStart : Int,
        menstruationFinish : Int,
        ovulationStart : Int,
        ovulationFinish : Int
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun changeColors(indicator : PartOfCycle)
}