package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.threeten.bp.LocalDate
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

    lateinit var lastMenstruation: LocalDate

    override suspend fun readQuizInfoAsync() = GlobalScope.async(Dispatchers.IO) {
        val day = sharedPreferences.getString(LAST_MENSTRUATION, "")
        if (day?.isNotEmpty() == true)
            lastMenstruation = LocalDate.parse(day)
        val averageTimeOfMenstruation = sharedPreferences.getInt(AVERAGE_TIME_OF_MENSTRUATION, -1)
        val averageTimeOfCycle = sharedPreferences.getInt(AVERAGE_TIME_OF_CYCLE, -1)
        val birthDate = Date(sharedPreferences.getLong(BIRTH_DATE, -1))

        if(this@ReadQuizAnswersRepositoryImpl::lastMenstruation.isInitialized || averageTimeOfMenstruation != -1 ||
            averageTimeOfCycle != -1 || birthDate.time != -1L){
            return@async QuizAnswers(lastMenstruation, averageTimeOfMenstruation, averageTimeOfCycle, birthDate)
        }

        return@async ReadQuizValidationError
    }
}