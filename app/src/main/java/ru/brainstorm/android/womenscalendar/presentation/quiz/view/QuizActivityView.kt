package ru.brainstorm.android.womenscalendar.presentation.quiz.view

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 17.10.2019
 */
interface QuizActivityView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setStep(step: Int)
}