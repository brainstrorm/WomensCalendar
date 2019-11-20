package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.w3c.dom.Text
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.MenuPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.NoteRedactorPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.NoteRedactorView

class NoteRedactorFragment : AbstractMenuFragment(), NoteRedactorView {

    private lateinit var textNoteRedactor : EditText
    private lateinit var dateNoteRedactor : TextView

    private lateinit var date: String
    private lateinit var text: String
    @InjectPresenter
    lateinit var noteRedactorPresenter: NoteRedactorPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().noteRedactorPresenter()

    companion object{
        val TAG = "NoteRedactor"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_redactor, container, false)

        textNoteRedactor = view.findViewById<EditText>(R.id.text_notes_redactor)
        dateNoteRedactor = view.findViewById<TextView>(R.id.date_notes_redactor)

        noteRedactorPresenter.viewState.setInformation()

        return inflater.inflate(R.layout.fragment_note_redactor, container, false)
    }

    fun provideInformation(date : String, text : String){
        this.date = date
        this.text = text
    }



    fun getDate() : String{
        return date
    }

    fun getText() : String{
        return view!!.findViewById<EditText>(R.id.text_notes_redactor).text.toString()
    }
    override fun getPart(): String = "note_redactor"

    override fun setInformation() {
        textNoteRedactor.setText(text)
        dateNoteRedactor.setText(date)
    }
}

