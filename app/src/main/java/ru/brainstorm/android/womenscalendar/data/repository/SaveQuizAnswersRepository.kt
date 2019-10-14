package ru.brainstorm.android.womenscalendar.data.repository

import kotlinx.coroutines.Job
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import java.util.Date

interface SaveQuizAnswersRepository {
    suspend fun saveInfo(quizAnswers : QuizAnswers): Job
}