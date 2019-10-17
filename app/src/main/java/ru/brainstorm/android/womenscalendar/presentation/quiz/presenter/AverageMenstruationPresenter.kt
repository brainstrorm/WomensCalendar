package ru.brainstorm.android.womenscalendar.presentation.quiz.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.AverageMenstruationView
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 17.10.2019
 */
@InjectViewState
class AverageMenstruationPresenter
    @Inject constructor()
        : MvpPresenter<AverageMenstruationView>() {
}