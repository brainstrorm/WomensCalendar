package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.WeekModeCalendarView
import javax.inject.Inject

@InjectViewState
class WeekModeCalendarPresenter
    @Inject constructor()
        :MvpPresenter<WeekModeCalendarView>()