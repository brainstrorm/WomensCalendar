package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.NoteRedactorFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.NoteRedactorView
import javax.inject.Inject

@InjectViewState
class NoteRedactorPresenter
    @Inject
    constructor()
        : MvpPresenter<NoteRedactorView>(){

    @Inject
    lateinit var noteDao: NoteDao

    fun saveNote(fragment : NoteRedactorFragment){
            var note = Note()
            note.noteDate = fragment.getDate()
            note.noteText = fragment.getText()
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    noteDao.insert(note)
                }
                job.join()
            }
    }
}
