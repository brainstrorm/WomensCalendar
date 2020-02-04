package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.NoteRedactorPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.NoteRedactorView
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import java.util.*


class NoteRedactorFragment : AbstractMenuFragment(), NoteRedactorView {

    private lateinit var textNoteRedactor : EditText
    private lateinit var dateNoteRedactor : TextView
    private lateinit var btnPen : ImageView

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE")
    private lateinit var date: String
    private lateinit var newDate : String
    private lateinit var text: String

    private val dateAndTime: Calendar? = Calendar.getInstance()
    @InjectPresenter
    lateinit var noteRedactorPresenter: NoteRedactorPresenter
    private lateinit var pref : SharedPreferences

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().noteRedactorPresenter()

    companion object{
        val TAG = "NoteRedactor"
    }

    private val d =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            newDate = LocalDate.parse("$year-" +
                    "${if(monthOfYear < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"}-" +
                    "${if(dayOfMonth < 10) "0${dayOfMonth}" else "${dayOfMonth}"}").toString()
            noteRedactorPresenter.viewState.setDate_()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_redactor, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        textNoteRedactor = view.findViewById<EditText>(R.id.text_notes_redactor)

        dateNoteRedactor = view.findViewById<TextView>(R.id.date_notes_redactor)

        btnPen = view.findViewById<ImageView>(R.id.bnt_pen_notes_redactor)

        btnPen.setOnClickListener {
            val fragmentPicker = DatePickerDialog(
                activity!!, d,
                LocalDate.parse(date).year,
                LocalDate.parse(date).monthValue - 1,
                LocalDate.parse(date).dayOfMonth
            )
            fragmentPicker.show()
        }
        noteRedactorPresenter.viewState.setInformation()


        return view
    }

    fun provideInformation(date : String, text : String){
        this.date = date
        this.newDate = date
        this.text = text
    }



    fun getDate() : String{
        return date
    }

    fun getText() : String{
        return view!!.findViewById<EditText>(R.id.text_notes_redactor).text.toString()
    }

    fun setDate() {
        this.date = newDate
    }

    fun getNewDate() : String = newDate

    override fun getPart(): String = "note_redactor"

    override fun setInformation() {
        val localDate = LocalDate.parse(date)
        textNoteRedactor.setText(text)
        view!!.findViewById<TextView>(R.id.date_notes_redactor).setText("${dayOfWeekFormatter.format(localDate)}," +
                " ${dateFormatter.format(localDate)} ${monthFormatter.format(localDate)} ${localDate.year} г.")
    }

    override fun setDate_() {
        val localDate = LocalDate.parse(newDate)
        view!!.findViewById<TextView>(R.id.date_notes_redactor).setText("${dayOfWeekFormatter.format(localDate)}," +
                " ${dateFormatter.format(localDate)} ${monthFormatter.format(localDate)} ${localDate.year} г.")
    }

}

