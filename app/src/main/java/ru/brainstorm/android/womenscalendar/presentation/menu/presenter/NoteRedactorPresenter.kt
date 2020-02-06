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
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    var note = Note()
                    note.noteText = ""
                    note.noteDate = ""
                    if(!fragment.getDate().equals(fragment.getNewDate()) && noteDao.getByDate(fragment.getDate()) != null){
                        noteDao.delete(noteDao.getByDate(fragment.getDate()))
                        fragment.setDate()
                    }else if (noteDao.getByDate(fragment.getDate()) != null){
                        noteDao.delete(noteDao.getByDate(fragment.getDate()))
                        fragment.setDate()
                    }else{
                        fragment.setDate()
                    }
                    note.noteDate = fragment.getDate()
                    note.noteText = fragment.getText()
                    noteDao.insert(note)
                }
                job.join()
            }
    }
}
