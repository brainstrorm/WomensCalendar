package ru.brainstorm.android.womenscalendar.presentation.quiz.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.QuizActivityView
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 17.10.2019
 */
@InjectViewState
class QuizActivityPresenter
    @Inject constructor()
        : MvpPresenter<QuizActivityView>() {
    fun provideStep(step: Int) = viewState.setStep(step)
}