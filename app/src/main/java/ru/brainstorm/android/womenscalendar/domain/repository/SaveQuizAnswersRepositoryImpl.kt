package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.data.repository.SaveQuizAnswersRepository

class SaveQuizAnswersRepositoryImpl(private val sharedPreferences: SharedPreferences)
    : SaveQuizAnswersRepository {

    companion object{
        private const val LAST_MENSTRUATION = "current.last_menstruation"
        private const val AVERAGE_TIME_OF_MENSTRUATION = "current.average_time_of_menstruation"
        private const val AVERAGE_TIME_OF_CYCLE = "current.average_time_of_cycle"
        private const val BIRTH_DATE = "current.birth_date"
    }

    override suspend fun saveInfo(quizAnswers : QuizAnswers) = GlobalScope.launch(Dispatchers.IO) {
        sharedPreferences.edit{
            clear()
            putString(LAST_MENSTRUATION, quizAnswers.lastMenstruation.toString())
            putInt(AVERAGE_TIME_OF_MENSTRUATION, quizAnswers.averageTimeOfMenstruation)
            putInt(AVERAGE_TIME_OF_CYCLE, quizAnswers.averageTimeOfCycle)
            putLong(BIRTH_DATE, quizAnswers.birthDate.time)
            apply()
        }
    }


}