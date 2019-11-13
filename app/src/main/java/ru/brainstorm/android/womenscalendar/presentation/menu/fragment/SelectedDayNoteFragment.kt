package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.brainstorm.android.womenscalendar.App

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import javax.inject.Inject

class SelectedDayNoteFragment : AbstractMenuFragment() {


    @Inject
    lateinit var noteDao: NoteDao

    private lateinit var textForNote : TextView
    private lateinit var dateForNote : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_selected_day_note, container, false)

        App.appComponent.inject(this)
        lateinit var setNote : List<Note>
        GlobalScope.async(Dispatchers.IO) {
            setNote = noteDao.getAll()
            return@async setNote
        }


        textForNote = view.findViewById(R.id.text_selected_day_note)
        dateForNote = view.findViewById(R.id.date_selcted_day_note)


        return view

    }

    override fun getPart(): String {
        return "selected_day_note"
    }

}
