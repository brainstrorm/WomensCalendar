package ru.brainstorm.android.womenscalendar.domain.repository

import java.time.LocalDate
import java.util.*

class QuizAnswersRepositoryImpl {
    companion object{
        const val LAST_MENSTRUATION = "current.last_menstruation"
        const val AVERAGE_TIME_OF_MENSTRUATION = 0
        const val AVERAGE_TIME_OF_CYCLE = 0
        val BIRTH_DATE = Date(System.currentTimeMillis())
    }
}