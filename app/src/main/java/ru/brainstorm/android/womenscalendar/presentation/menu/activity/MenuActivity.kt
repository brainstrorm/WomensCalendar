package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import moxy.MvpAppCompatActivity
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment

class MenuActivity : MvpAppCompatActivity() {



    companion object {

        const val TAG = "Today"

        fun provideIntent(packageContext: Context) = Intent(packageContext, MenuActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction()
            .add(R.id.for_fragment, WeekModeCalendarFragment())//
            .commit()
    }
}
