package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear
import androidx.core.content.edit
import ru.brainstorm.android.womenscalendar.data.repository.SaveQuizAnswersRepository
import java.time.LocalDate
import java.util.*

class QuizAnswersRepositoryImpl(private val sharedPreferences: SharedPreferences) : SaveQuizAnswersRepository{
    companion object{
        val last_menstruation = Date(System.currentTimeMillis())
        const val AVERAGE_TIME_OF_MENSTRUATION = 0
        const val AVERAGE_TIME_OF_CYCLE = 0
        val birth_date = Date(System.currentTimeMillis())
    }

    override fun saveInfo() {
        sharedPreferences.edit{
            clear()
            putLong("last_menstruation",last_menstruation.time)
            putInt("AVERAGE_TIMR_OF_MENSTRUATION", AVERAGE_TIME_OF_MENSTRUATION)
            putInt("AVERAGE_TIME_OF_CYCLE", AVERAGE_TIME_OF_CYCLE)
            putLong("birth_date", birth_date.time)
            apply()
        }
    }

}