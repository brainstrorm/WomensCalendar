package ru.brainstorm.android.womenscalendar.presentation.splash.presenter

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.data.User
import ru.brainstorm.android.womenscalendar.domain.repository.ReadUserInfoRepositoryImpl
import ru.brainstorm.android.womenscalendar.presentation.splash.view.SplashScreenView
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 15.10.2019
 */
@InjectViewState
class SplashScreenPresenter
    @Inject constructor(private val readUserInfoRepository: ReadUserInfoRepositoryImpl)
        : MvpPresenter<SplashScreenView>() {

    fun checkFirstLaunch() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1_000)
            readUserInfoRepository.readUserInfoAsync().join()
            if (User.isInitialized()) {
                viewState.goToCalendar()
            } else {
                //DEBUG VERSION
                User.firebaseId = "kek"
                //                 ^
                //DELETE IN RELEASE|
                viewState.goToQuiz()
            }
        }
    }

}