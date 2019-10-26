package ru.brainstorm.android.womenscalendar.presentation.quiz.fragment

import androidx.fragment.app.Fragment
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 17.10.2019
 */
abstract class AbstractQuizFragment : Fragment() {
    abstract fun getNextFragment(): AbstractQuizFragment?

    abstract fun getPrevFragment(): AbstractQuizFragment?

    abstract fun getStep(): Int

    abstract fun setQuizAns(cycle: Cycle)
}