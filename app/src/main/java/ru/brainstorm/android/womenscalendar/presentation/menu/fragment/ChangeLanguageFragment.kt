package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
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
import java.util.prefs.PreferencesFactory
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

    private lateinit var pref : SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_language, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)


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

        when(pref.getString("language", "en")){
            "en" -> checkMarks[0].isVisible = true
            "ru" -> checkMarks[1].isVisible = true
            "es" -> checkMarks[2].isVisible = true
            "pt" -> checkMarks[3].isVisible = true
            "th" -> checkMarks[4].isVisible = true
            "vi" -> checkMarks[5].isVisible = true
            "zh" -> checkMarks[6].isVisible = true
        }

        englishButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            english_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("en")
        }
        russianButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            russian_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("ru")
        }
        spanishButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            spanish_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("es")
        }
        portugueseButton.setOnClickListener { view ->
            checkMarks.forEach{
                it.isVisible = false
            }
            portugese_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("pt")
        }
        thaiButton.setOnClickListener { view ->
            checkMarks.forEach {
                it.isVisible = false
            }
            thai_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("th")
        }
        vietnameseButton.setOnClickListener { view ->
            checkMarks.forEach {
                it.isVisible = false
            }
            vietnamese_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("vi")
        }
        chineseButton.setOnClickListener { view ->
            checkMarks.forEach {
                it.isVisible = false
            }
            chinese_check_mark.isVisible = true
            (activity as MenuActivity).changeLocale("zh")
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


}
