package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_language.*
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.util.*
import kotlin.collections.ArrayList


class ChangeLanguageFragment : Fragment() {

    private lateinit var englishButton : ImageButton
    private lateinit var russianButton : ImageButton
    private lateinit var spanishButton : ImageButton
    private lateinit var portugueseButton : ImageButton
    private lateinit var thaiButton : ImageButton
    private lateinit var vietnameseButton : ImageButton
    private lateinit var chineseButton : ImageButton
    private lateinit var checkMarks : ArrayList<ImageView>
    private lateinit var backButton : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_language, container, false)
        checkMarks = ArrayList()
        checkMarks.add(view.findViewById(R.id.english_check_mark))
        checkMarks.add(view.findViewById(R.id.russian_check_mark))
        checkMarks.add(view.findViewById(R.id.spanish_check_mark))
        checkMarks.add(view.findViewById(R.id.portugese_check_mark))
        checkMarks.add(view.findViewById(R.id.thai_check_mark))
        checkMarks.add(view.findViewById(R.id.vietnamese_check_mark))
        checkMarks.add(view.findViewById(R.id.chinese_check_mark))
        englishButton = view.findViewById<ImageButton>(R.id.english_button)
        russianButton = view.findViewById<ImageButton>(R.id.russian_button)
        spanishButton = view.findViewById<ImageButton>(R.id.spanish_button)
        portugueseButton = view.findViewById<ImageButton>(R.id.portuguese_button)
        thaiButton = view.findViewById<ImageButton>(R.id.thai_button)
        vietnameseButton = view.findViewById<ImageButton>(R.id.vietnamese_button)
        chineseButton = view.findViewById<ImageButton>(R.id.chinese_button)
        backButton = activity!!.findViewById<ImageView>(R.id.btn_back)

        englishButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            english_check_mark.isVisible = true
            changeLocale("en")
        }
        russianButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            russian_check_mark.isVisible = true
            changeLocale("rus")
        }
        spanishButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            spanish_check_mark.isVisible = true
            changeLocale("es")
        }
        portugueseButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            portugese_check_mark.isVisible = true
            changeLocale("pt")
        }
        thaiButton.setOnClickListener { view ->
            checkMarks.forEach {
                it.isVisible = false
            }
            thai_check_mark.isVisible = true
            changeLocale("th")
        }
        vietnameseButton.setOnClickListener { view ->
            checkMarks.forEach {
                it.isVisible = false
            }
            vietnamese_check_mark.isVisible = true
            changeLocale("vi")
        }
        chineseButton.setOnClickListener { view ->
            checkMarks.forEach {
                it.isVisible = false
            }
            chinese_check_mark.isVisible = true
            changeLocale("zh")
        }
        backButton.setOnClickListener {view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun changeLocale(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context!!.getResources().updateConfiguration(configuration, null)
        var txtvwToday =  activity!!.findViewById<TextView>(R.id.today_text)
        var txtvwCalendar = activity!!.findViewById<TextView>(R.id.calendar_text)
        var txtvwNotes = activity!!.findViewById<TextView>(R.id.info_text)
        var txtvwMore = activity!!.findViewById<TextView>(R.id.more_text)
        var txtvwNotesHeader = activity!!.findViewById<TextView>(R.id.notes)
        var txtvwCanceled = activity!!.findViewById<TextView>(R.id.btn_canceled)
        var txtvwSave = activity!!.findViewById<TextView>(R.id.btn_save)
        var txtvwYear = activity!!.findViewById<TextView>(R.id.text_month)
        var txtvwMonth = activity!!.findViewById<TextView>(R.id.text_year)
        var txtvwChangeLanguage = activity!!.findViewById<TextView>(R.id.change_language)
        txtvwToday.setText(R.string.today)
        txtvwCalendar.setText(R.string.calendar)
        txtvwNotes.setText(R.string.notes)
        txtvwMore.setText(R.string.more)
        txtvwNotesHeader.setText(R.string.notes)
        txtvwCanceled.setText(R.string.cancel)
        txtvwSave.setText(R.string.btn_save_menu)
        txtvwYear.setText(R.string.btn_year)
        txtvwMonth.setText(R.string.btn_month)
        txtvwChangeLanguage.setText(R.string.change_language)
    }

}
