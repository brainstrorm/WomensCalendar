package ru.brainstorm.android.womenscalendar.presentation.quiz.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.AverageMenstruationFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.CalendarPickerForQuizFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.QuizActivityPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.QuizActivityView


class QuizActivity : MvpAppCompatActivity(), QuizActivityView, View.OnClickListener {

    @InjectPresenter
    lateinit var quizPresenter: QuizActivityPresenter

    lateinit var questions: Array<String>

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().quizActivityPresenter()

    companion object {

        const val TAG = "Quiz"

        fun provideIntent(packageContext: Context) = Intent(packageContext, QuizActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poll)
        questions = resources.getStringArray(R.array.quiz_questions)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction()
            .add(R.id.picker, CalendarPickerForQuizFragment())
            .commit()
        initFragments()
        findViewById<View>(R.id.btn_next_middle).setOnClickListener(this)
        findViewById<View>(R.id.btn_next).setOnClickListener(this)
        findViewById<View>(R.id.btn_dnt_remember_white).setOnClickListener(this)
        findViewById<View>(R.id.btn_dnt_remember).setOnClickListener(this)
        findViewById<View>(R.id.arrow).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0 ?: return
        when(p0.id) {
            R.id.btn_next_middle, R.id.btn_next -> {
                performNext(save = true)
            }
            R.id.btn_dnt_remember, R.id.btn_dnt_remember_white -> {
                performNext(save = false)
            }
            //It is back btn
            R.id.arrow -> {
                performPrev()
            }
        }
    }

    private fun performPrev() {
        quizPresenter.performedPrev(supportFragmentManager)
    }

    private fun performNext(save: Boolean) {
        quizPresenter.performedNext(supportFragmentManager, save)
    }

    private fun initFragments() {
        quizPresenter.provideStep(0)
    }

    override fun setStep(step: Int) {


        findViewById<TextView>(R.id.step_poll).apply {
            text = getString(R.string.step_poll, step + 1)
        }
        findViewById<TextView>(R.id.text_poll).apply {
            text = questions[step]
        }

        findViewById<ImageView>(R.id.rect_main).apply {

            when(step) {

                0 ->  this.background = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar1 ,null)
                1 ->  this.background = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar2 ,null)
                2 ->  this.background = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar3 ,null)
                3 ->  this.background = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar4 ,null)

            }
          }
    }

    override fun navigateToCalculation() {

    }

}
