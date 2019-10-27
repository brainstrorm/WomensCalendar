package ru.brainstorm.android.womenscalendar.data.repository

import kotlinx.coroutines.Job
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle

interface SaveQuizAnswersRepository {
    suspend fun saveInfo(cycle: Cycle): Job
}