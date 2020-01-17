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
import kotlinx.android.synthetic.main.activity_splash_screen.*

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity

class AboutAppFragment : AbstractMenuFragment(){
    private lateinit var btnBack : ImageView

    override fun getPart(): String = "about_app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mainView = inflater.inflate(R.layout.fragment_about_app, container, false)
        initViews()
        return mainView
    }

    private fun initViews(){
        btnBack = activity!!.findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener { view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
    }
}
