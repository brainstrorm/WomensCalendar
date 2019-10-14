package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.data.repository.SaveQuizAnswersRepository
import java.util.*

class QuizAnswersRepositoryImpl(private val sharedPreferences: SharedPreferences)
    : SaveQuizAnswersRepository {

    companion object{
        private const val LAST_MENSTRUATION = "current.last_menstruation"
        private const val AVERAGE_TIME_OF_MENSTRUATION = "current.average_time_of_menstruation"
        private const val AVERAGE_TIME_OF_CYCLE = "current.average_time_of_cycle"
        private const val BIRTH_DATE = "current.birth_date"
    }

    override suspend fun saveInfo(lastMenstruation: Date,
                          averageTimeOfMenstruation: Int,
                          averageTimeOfCycle: Int,
                          birthDate: Date) = GlobalScope.launch(Dispatchers.IO) {
        sharedPreferences.edit{
            clear()
            putLong(LAST_MENSTRUATION, lastMenstruation.time)
            putInt(AVERAGE_TIME_OF_MENSTRUATION, averageTimeOfMenstruation)
            putInt(AVERAGE_TIME_OF_CYCLE, averageTimeOfCycle)
            putLong(BIRTH_DATE, birthDate.time)
            apply()
        }
    }


}