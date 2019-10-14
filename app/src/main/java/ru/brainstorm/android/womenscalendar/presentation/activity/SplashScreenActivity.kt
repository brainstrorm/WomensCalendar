package ru.brainstorm.android.womenscalendar.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import moxy.MvpView
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.view.SplashScreenView

class SplashScreenActivity : AppCompatActivity(), SplashScreenView {

    override fun goToQuiz() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToCalendar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
    }

}
