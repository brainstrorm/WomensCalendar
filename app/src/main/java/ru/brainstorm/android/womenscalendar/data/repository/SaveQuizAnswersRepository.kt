package ru.brainstorm.android.womenscalendar.data.repository

import java.util.*

interface SaveQuizAnswersRepository {
    fun saveInfo(lastMenstruation: Date, averageTimeOfMenstruation: Int, averageTimeOfCycle: Int, birthDate: Date)
}