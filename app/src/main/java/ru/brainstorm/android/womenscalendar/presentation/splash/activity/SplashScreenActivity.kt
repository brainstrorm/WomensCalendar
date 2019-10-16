package ru.brainstorm.android.womenscalendar.presentation.splash.activity

import android.content.Intent
import android.os.Bundle
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import ru.brainstorm.android.womenscalendar.presentation.splash.presenter.SplashScreenPresenter
import ru.brainstorm.android.womenscalendar.presentation.splash.view.SplashScreenView

class SplashScreenActivity : MvpAppCompatActivity(), SplashScreenView {

    @InjectPresenter
    internal lateinit var splashScreenPresenter: SplashScreenPresenter

    @ProvidePresenter
    fun providePresenter(): SplashScreenPresenter = App.appComponent.presenter().splashPresenter()

    override fun goToQuiz() {
    }

    override fun goToCalendar() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this@SplashScreenActivity)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        splashScreenPresenter.checkFirstLaunch()
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }

}
