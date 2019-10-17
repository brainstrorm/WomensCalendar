package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import moxy.MvpAppCompatFragment
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 17.10.2019
 */
abstract class AbstractQuizFragment : MvpAppCompatFragment() {
    abstract fun getNextFragment(): AbstractQuizFragment?

    abstract fun getPrevFragment(): AbstractQuizFragment?

    abstract fun getStep(): Int

    abstract fun setQuizAns(ans: QuizAnswers)
}