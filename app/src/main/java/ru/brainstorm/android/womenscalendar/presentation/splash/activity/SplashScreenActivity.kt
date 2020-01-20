package ru.brainstorm.android.womenscalendar.presentation.splash.activity

import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.TextView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import ru.brainstorm.android.womenscalendar.presentation.splash.presenter.SplashScreenPresenter
import ru.brainstorm.android.womenscalendar.presentation.splash.view.SplashScreenView
import java.util.*

class SplashScreenActivity : MvpAppCompatActivity(), SplashScreenView {

    private lateinit var txtvwInit : TextView

    private lateinit var pref : SharedPreferences

    @InjectPresenter
    internal lateinit var splashScreenPresenter: SplashScreenPresenter

    @ProvidePresenter
    fun providePresenter(): SplashScreenPresenter = App.appComponent.presenter().splashPresenter()

    override fun goToQuiz() {
        startActivity(QuizActivity.provideIntent(this@SplashScreenActivity))
    }

    override fun goToCalendar() {
        startActivity(MenuActivity.provideIntent(this@SplashScreenActivity))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this@SplashScreenActivity)
        setContentView(R.layout.activity_splash_screen)

        val bar =  this.supportActionBar
        bar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorForActionBar)))

        supportActionBar?.hide()

        txtvwInit = findViewById<TextView>(R.id.init)

        pref = PreferenceManager.getDefaultSharedPreferences(this)

        updateLocale()

        splashScreenPresenter.checkFirstLaunch()
    }
    fun updateLocale(){
        if(pref.getString("language", "def").equals("def")) {
            val APP_LANGUAGE = "language"
            val editor = pref.edit()
            editor.putString(APP_LANGUAGE, Locale.getDefault().getLanguage())
            editor.commit()
        }
        val locale = Locale(pref.getString("language", "en")!!)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwInit.setText(R.string.init_rus)
    }

}
