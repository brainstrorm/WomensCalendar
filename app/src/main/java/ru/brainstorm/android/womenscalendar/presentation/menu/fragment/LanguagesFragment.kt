package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_languages.*

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.util.*


class LanguagesFragment : AbstractMenuFragment() {

    private lateinit var languagesButton : ImageButton
    private lateinit var backButton : ImageView
    private lateinit var pref : SharedPreferences
    private lateinit var txtvwLanguages : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_languages, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)
        txtvwLanguages = view.findViewById<TextView>(R.id.languages_text)
        updateLocale()

        languagesButton = view.findViewById<ImageButton>(R.id.languages)
        backButton = activity!!.findViewById<ImageView>(R.id.btn_back)
        languagesButton.setOnClickListener{view ->
            (activity as MenuActivity).apply {
                menuPresenter.addFragmentToBackStack(this@LanguagesFragment)
                menuPresenter.setFragment(supportFragmentManager, "change_language") }
        }
        backButton.setOnClickListener {view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun getPart(): String = "languages"


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)
        txtvwLanguages.setText(R.string.languages)
    }
}
