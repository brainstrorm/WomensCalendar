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
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.App

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.CalendarPickerPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.SelectedDayNotePresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.SelectedDayNoteView
import javax.inject.Inject

class SelectedDayNoteFragment : AbstractMenuFragment(), SelectedDayNoteView {

    @InjectPresenter
    lateinit var selectedDayNotePresenter: SelectedDayNotePresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().selectedDayNotePresenter()

    @Inject
    lateinit var noteDao: NoteDao

    private lateinit var date : String
    private lateinit var textForNote : TextView
    private lateinit var dateForNote : TextView

    companion object{
        val TAG = "SelectedDayNote"
    }

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


        textForNote = view.findViewById<TextView>(R.id.text_selected_day_note)
        dateForNote = view.findViewById<TextView>(R.id.date_selcted_day_note)

        selectedDayNotePresenter.setInformationFromCalendar(date)

        return view

    }

    fun getDate() : String{
        return date
    }

    fun getText() : String{
        return textForNote.text.toString()
    }
    override fun getPart(): String {
        return "selected_day_note"
    }

    override fun setInformation(date: String, text: String) {
        dateForNote.setText(date)
        textForNote.setText(text)
    }

    fun provideDate(date: String){
        this.date = date
    }
}
