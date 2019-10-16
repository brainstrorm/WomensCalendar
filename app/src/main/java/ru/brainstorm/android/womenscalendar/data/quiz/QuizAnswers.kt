package ru.brainstorm.android.womenscalendar.data.quiz

import java.util.Date

/*
 * sealed class - like enum class, but it allows you
 * to extend it and provide params\methods to this class
 */
sealed class ReadQuizAnswersResult

//This class returned if we successfully read our data
data class QuizAnswers(val lastMenstruation : Date,
                       val averageTimeOfMenstruation : Int,
                       val averageTimeOfCycle : Int,
                       val birthDate : Date): ReadQuizAnswersResult()

/*
 * This object returned if there were any problems
 * It is object because it only signals to us, that there were some troubles
 * So there is no need to create instance
 */
object ReadQuizValidationError : ReadQuizAnswersResult()