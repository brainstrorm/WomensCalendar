package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import moxy.InjectViewState
import moxy.MvpPresenter
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.presentation.menu.view.SelectedDayNoteView
import javax.inject.Inject

@InjectViewState
class SelectedDayNotePresenter
    @Inject
    constructor()
        : MvpPresenter<SelectedDayNoteView>(){

    @Inject
    lateinit var noteDao: NoteDao

    fun setInformationFromCalendar(date : String){
        var note : Note
        GlobalScope.async(Dispatchers.IO) {
            note = noteDao.getByDate(date.toString())
            if(note != null){
                viewState.setInformation(note.noteDate, note.noteText)
            }else{
                viewState.setInformation(date, "")
            }
            return@async note
        }
    }
}
