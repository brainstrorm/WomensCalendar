package ru.brainstorm.android.womenscalendar.presentation.quiz.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.fragment_average_menstruation.*
import moxy.MvpAppCompatActivity
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.AverageMenstruationFragment


class QuizActivity : MvpAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val fm = supportFragmentManager
        fm.beginTransaction()
            .add(R.id.picker, AverageMenstruationFragment())
            .commit()

    }

}
