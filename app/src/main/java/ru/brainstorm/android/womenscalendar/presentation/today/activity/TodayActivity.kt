package ru.brainstorm.android.womenscalendar.presentation.today.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import moxy.MvpAppCompatActivity
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.today.fragment.WeekModeCalendarFragment

class TodayActivity : MvpAppCompatActivity() {



    companion object {

        const val TAG = "Today"

        fun provideIntent(packageContext: Context) = Intent(packageContext, TodayActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today)
        supportFragmentManager.beginTransaction()
            .add(R.id.picker, WeekModeCalendarFragment())
            .commit()
    }
}
