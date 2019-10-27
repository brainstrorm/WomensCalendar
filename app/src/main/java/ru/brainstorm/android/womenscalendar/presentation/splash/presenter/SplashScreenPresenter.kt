package ru.brainstorm.android.womenscalendar.presentation.splash.presenter

import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.data.User
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.domain.repository.ReadUserInfoRepositoryImpl
import ru.brainstorm.android.womenscalendar.presentation.splash.view.SplashScreenView
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 15.10.2019
 */
@InjectViewState
class SplashScreenPresenter
    @Inject constructor(
        private val readUserInfoRepository: ReadUserInfoRepositoryImpl,
        private val cycleDao: CycleDao)
        : MvpPresenter<SplashScreenView>() {

    fun checkFirstLaunch() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(1_000)
            val readJob = readUserInfoRepository.readUserInfoAsync()
            val cycles = cycleDao.getAll()
            readJob.join()
            if (User.isInitialized() && cycles.isNotEmpty()) {
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