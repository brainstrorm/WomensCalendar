package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.data.quiz.ReadQuizValidationError
import ru.brainstorm.android.womenscalendar.data.repository.ReadQuizAnswersRepository
import java.util.*

class ReadQuizAnswersRepositoryImpl(private val sharedPreferences: SharedPreferences) : ReadQuizAnswersRepository{

    companion object{
        private const val LAST_MENSTRUATION = "current.last_menstruation"
        private const val AVERAGE_TIME_OF_MENSTRUATION = "current.average_time_of_menstruation"
        private const val AVERAGE_TIME_OF_CYCLE = "current.average_time_of_cycle"
        private const val BIRTH_DATE = "current.birth_date"
    }

    override suspend fun readQuizInfoAsync() = GlobalScope.async(Dispatchers.IO) {

        val lastMenstruation = Date(sharedPreferences.getLong(LAST_MENSTRUATION, -1))
        val averageTimeOfMenstruation = sharedPreferences.getInt(AVERAGE_TIME_OF_MENSTRUATION, -1)
        val averageTimeOfCycle = sharedPreferences.getInt(AVERAGE_TIME_OF_CYCLE, -1)
        val birthDate = Date(sharedPreferences.getLong(BIRTH_DATE, -1))

        if(lastMenstruation.time != -1L && averageTimeOfMenstruation != -1 &&
            averageTimeOfCycle != -1 && birthDate.time != -1L){
            return@async QuizAnswers(lastMenstruation, averageTimeOfMenstruation, averageTimeOfCycle, birthDate)
        }

        return@async ReadQuizValidationError
    }
}