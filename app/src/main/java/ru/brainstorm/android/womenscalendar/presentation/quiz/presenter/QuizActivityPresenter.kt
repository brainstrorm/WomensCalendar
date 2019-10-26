package ru.brainstorm.android.womenscalendar.presentation.quiz.presenter

import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.domain.repository.SaveQuizAnswersRepositoryImpl
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.AbstractQuizFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.BirthDateFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.view.QuizActivityView
import java.util.*
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 17.10.2019
 */
@InjectViewState
class QuizActivityPresenter
    @Inject constructor(private val saveQuizAnswersRepositoryImpl: SaveQuizAnswersRepositoryImpl)
        : MvpPresenter<QuizActivityView>() {

    private val quizAnswers = QuizAnswers(LocalDate.MIN, -1, -1, Date(-1))

    fun provideStep(step: Int) = viewState.setStep(step)

    private fun saveResults() = GlobalScope.launch(Dispatchers.Main) {
        saveQuizAnswersRepositoryImpl.saveInfo(quizAnswers).join()
        viewState.navigateToCalculation()
    }

    fun performedPrev(fm: FragmentManager) {
        val fragment = fm.findFragmentById(R.id.picker) as? AbstractQuizFragment
        fragment ?: return
        fragment.getPrevFragment()?.apply {
            fm.beginTransaction()
                .replace(R.id.picker, this)
                .commit()
            viewState.setStep(getStep())
        }
    }

    fun performedNext(fm: FragmentManager, save: Boolean) {
        val fragment = fm.findFragmentById(R.id.picker) as? AbstractQuizFragment
        fragment ?: return
        if (save) {
            fragment.setQuizAns(quizAnswers)
            if (fragment is BirthDateFragment) {
                saveResults()
                return
            }
        }
        fragment.getNextFragment()?.apply {
            fm.beginTransaction()
                .replace(R.id.picker, this)
                .commit()
            viewState.setStep(getStep())
        }
    }

}