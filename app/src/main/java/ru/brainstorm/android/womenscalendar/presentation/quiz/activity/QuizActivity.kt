package ru.brainstorm.android.womenscalendar.presentation.quiz.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.poll_init.view.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.*
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.QuizActivityPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.QuizActivityView


class QuizActivity : MvpAppCompatActivity(), QuizActivityView, View.OnClickListener {

    @InjectPresenter
    lateinit var quizPresenter: QuizActivityPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().quizActivityPresenter()

    companion object {
        fun provideIntent(packageContext: Context) = Intent(packageContext, QuizActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poll)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction()
            .add(R.id.picker, CalendarPickerForQuizFragment())
            .commit()
        provideStep(1)
    }

    override fun onClick(p0: View?) {
        p0 ?: return
        when(p0.id) {
            R.id.btn_next_middle, R.id.btn_next -> {
                performNext()
            }
            R.id.btn_dnt_remember, R.id.btn_dnt_remember_white -> {
                performNext()
            }
            //It is back btn
            R.id.arrow -> {
                performPrev()
            }
        }
    }

    private fun performPrev() = supportFragmentManager.also {
        val fragment = it.findFragmentById(R.id.picker) as? AbstractQuizFragment
        fragment ?: return@also
        fragment.getPrevFragment()?.apply {
            it.beginTransaction()
                .replace(R.id.picker, this)
                .commit()
            this@QuizActivity.provideStep(getStep())
        }
    }

    private fun performNext() = supportFragmentManager.also {
        val fragment = it.findFragmentById(R.id.picker) as? AbstractQuizFragment
        fragment ?: return@also
        fragment.getNextFragment()?.apply {
            it.beginTransaction()
                .replace(R.id.picker, this)
                .commit()
        }
    }

    private fun provideStep(step: Int) {
        quizPresenter.provideStep(step)
    }

    override fun setStep(step: Int) {


        findViewById<TextView>(R.id.step_poll).apply {
            text = getString(R.string.step_poll, step)

        }

        findViewById<TextView>(R.id.text_poll).apply {
            val array: Array<String> =  resources.getStringArray(R.array.poll_texts)
            text = array[step]
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

}
