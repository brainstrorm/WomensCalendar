package ru.brainstorm.android.womenscalendar.presentation.splash.view

import com.google.android.gms.ads.AdListener
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface SplashScreenView : MvpView{
    fun goToQuiz()
    fun goToCalendar()
}