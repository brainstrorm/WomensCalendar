package ru.brainstorm.android.womenscalendar.presentation.initialization.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.initialization.presenter.InitializationActivityPresenter
import ru.brainstorm.android.womenscalendar.presentation.initialization.view.InitializationActivityView
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.quiz.activity.QuizActivity
import ru.brainstorm.android.womenscalendar.presentation.splash.presenter.SplashScreenPresenter
import android.graphics.drawable.ColorDrawable
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.preference.PreferenceManager
import android.widget.TextView
import java.util.*


class InitializationActivity :  MvpAppCompatActivity(), InitializationActivityView {

    @InjectPresenter
    internal lateinit var initializationActivityPresenter: InitializationActivityPresenter

    private lateinit var pref : SharedPreferences

    companion object {

        const val TAG = "Initialization"

        fun provideIntent(packageContext: Context) = Intent(packageContext, InitializationActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poll_init)
        val bar = supportActionBar
        bar!!.setBackgroundDrawable(ColorDrawable( resources.getColor(R.color.colorForActionBar)))
        initializationActivityPresenter.waiting()

        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun goToMenu() {
        startActivity(MenuActivity.provideIntent(this@InitializationActivity))
    }

    override fun Predictor() {
        TODO("not implemented") //To change body of created functions use File | SettingsFragment | File Templates.
    }


}
