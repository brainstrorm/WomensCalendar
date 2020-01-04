package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.brainstorm.android.womenscalendar.R
import java.util.*


class ChangeLanguageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val locale = Locale("en")
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context!!.getResources().updateConfiguration(configuration, null)
        var txtvwToday =  activity!!.findViewById<TextView>(R.id.today_text)
        txtvwToday.setText(R.string.today)
        return inflater.inflate(R.layout.fragment_change_language, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

}
