package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ListOfNotesView
import javax.inject.Inject

@InjectViewState
class ListOfNotesPresenter
@Inject constructor()
    : MvpPresenter<ListOfNotesView>(){

    fun goToChosenNote(){
        //TODO
    }
}