package ru.brainstorm.android.womenscalendar.presentation.quiz.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.CalendarPickerForQuizView
import javax.inject.Inject

@InjectViewState
class CalendarPickerForQuizPresenter
    @Inject constructor()
        :MvpPresenter<CalendarPickerForQuizView>()
