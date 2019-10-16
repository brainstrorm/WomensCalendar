package ru.brainstorm.android.womenscalendar.data.repository

import kotlinx.coroutines.Deferred
import ru.brainstorm.android.womenscalendar.data.quiz.ReadQuizAnswersResult

interface ReadQuizAnswersRepository{
    suspend fun readQuizInfoAsync() : Deferred<ReadQuizAnswersResult>
}