package ru.brainstorm.android.womenscalendar.presentation.quiz.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.AverageCycleView
import javax.inject.Inject

@InjectViewState
class AverageCyclePresenter
    @Inject constructor()
        : MvpPresenter<AverageCycleView>(){
        }