package ru.brainstorm.android.womenscalendar.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import moxy.MvpAppCompatActivity
import ru.brainstorm.android.womenscalendar.R

class SplashScreenActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }

}
