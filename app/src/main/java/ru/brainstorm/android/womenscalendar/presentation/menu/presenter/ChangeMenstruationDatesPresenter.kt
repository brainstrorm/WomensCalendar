package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ChangeMenstruationDatesView
import javax.inject.Inject

@InjectViewState
class ChangeMenstruationDatesPresenter
@Inject
constructor()
    : MvpPresenter<ChangeMenstruationDatesView>(){
}