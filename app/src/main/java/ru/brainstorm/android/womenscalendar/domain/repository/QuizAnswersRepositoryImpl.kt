package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear
import androidx.core.content.edit
import ru.brainstorm.android.womenscalendar.data.repository.SaveQuizAnswersRepository
import java.time.LocalDate
import java.util.*

class QuizAnswersRepositoryImpl(private val sharedPreferences: SharedPreferences) : SaveQuizAnswersRepository{
    companion object{
        const val LAST_MENSTRUATION = "current.last_menstruation"
        const val AVERAGE_TIME_OF_MENSTRUATION = "current.average_time_of_menstruation"
        const val AVERAGE_TIME_OF_CYCLE = "current.average_time_of_cycle"
        const val BIRTH_DATE = "current.birth_date"
    }

    override fun saveInfo(lastMenstruation: Date, averageTimeOfMenstruation: Int, averageTimeOfCycle: Int, birthDate: Date) {
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