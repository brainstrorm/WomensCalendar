package ru.brainstorm.android.womenscalendar.presentation.splash.presenter

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.data.quiz.QuizAnswers
import ru.brainstorm.android.womenscalendar.data.quiz.ReadQuizValidationError
import ru.brainstorm.android.womenscalendar.domain.repository.ReadQuizAnswersRepositoryImpl
import ru.brainstorm.android.womenscalendar.presentation.splash.view.SplashScreenView
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 15.10.2019
 */
@InjectViewState
class SplashScreenPresenter
    @Inject constructor(private val readQuizAnswersRepository: ReadQuizAnswersRepositoryImpl)
        : MvpPresenter<SplashScreenView>() {

    fun checkFirstLaunch() {
        GlobalScope.launch(Dispatchers.Main) {
            when (readQuizAnswersRepository.readQuizInfoAsync().await()) {
                is ReadQuizValidationError -> {
                    viewState.goToQuiz()
                }
                is QuizAnswers -> {
                    viewState.goToCalendar()
                }
            }
        }
    }

}