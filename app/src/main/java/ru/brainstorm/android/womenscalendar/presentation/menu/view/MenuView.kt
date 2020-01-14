package ru.brainstorm.android.womenscalendar.presentation.menu.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MenuView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun goToStatistic()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun goToRateUs()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setPart(part: String)
}