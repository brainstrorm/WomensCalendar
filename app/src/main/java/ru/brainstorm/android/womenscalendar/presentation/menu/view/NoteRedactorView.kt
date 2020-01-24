package ru.brainstorm.android.womenscalendar.presentation.menu.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NoteRedactorView : MvpView{

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setInformation()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setDate()
}