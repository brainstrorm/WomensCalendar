package ru.brainstorm.android.womenscalendar.data.repository

import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers

interface ReadQuizAnswersRepository{
    fun readQuizInfo() : QuizAnswers
}