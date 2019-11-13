package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import android.content.Context
import android.content.Intent
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ListOfNotesView
import ru.brainstorm.android.womenscalendar.presentation.note_redactor.activity.NoteRedactorActivity
import javax.inject.Inject

@InjectViewState
class ListOfNotesPresenter
@Inject constructor()
    : MvpPresenter<ListOfNotesView>(){

    fun goToChosenNote(context: Context) : Intent {
        return NoteRedactorActivity.provideIntent(context)
    }
}