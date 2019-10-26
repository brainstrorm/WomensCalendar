package ru.brainstorm.android.womenscalendar.presentation.initialization.view

import kotlinx.coroutines.Delay
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface InitializationActivityView : MvpView {
    fun Predictor()

    fun goToMenu()

}