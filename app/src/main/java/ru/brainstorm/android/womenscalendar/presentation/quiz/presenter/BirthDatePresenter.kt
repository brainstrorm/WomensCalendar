package ru.brainstorm.android.womenscalendar.presentation.quiz.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.BirthDateView
import javax.inject.Inject

@InjectViewState
class BirthDatePresenter
    @Inject constructor()
        :MvpPresenter<BirthDateView>(){}