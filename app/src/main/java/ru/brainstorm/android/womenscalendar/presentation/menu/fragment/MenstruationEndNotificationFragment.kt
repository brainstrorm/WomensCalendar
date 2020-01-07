package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity

class MenstruationEndNotificationFragment : AbstractMenuFragment() {

    private lateinit var btnBack : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menstruation_end_notification, container, false)
        btnBack = activity!!.findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
        return view
    }

    override fun getPart(): String = "start_of_menstruation"

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
