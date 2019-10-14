package ru.brainstorm.android.womenscalendar.data.repository

import kotlinx.coroutines.Job
import java.util.Date

interface SaveQuizAnswersRepository {
    suspend fun saveInfo(lastMenstruation: Date,
                 averageTimeOfMenstruation: Int,
                 averageTimeOfCycle: Int,
                 birthDate: Date): Job
}