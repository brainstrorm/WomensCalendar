package ru.brainstorm.android.womenscalendar.data.quiz

import java.util.*

data class QuizAnswers(val lastMenstruation : Date,
                       val averageTimeOfMenstruation : Int,
                       val averageTimeOfCycle : Int,
                       val birthDate : Date)