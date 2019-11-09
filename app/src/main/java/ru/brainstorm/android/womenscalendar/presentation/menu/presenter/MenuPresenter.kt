package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.domain.repository.ReadUserInfoRepositoryImpl
import ru.brainstorm.android.womenscalendar.presentation.initialization.view.InitializationActivityView
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import ru.brainstorm.android.womenscalendar.presentation.splash.view.SplashScreenView
import javax.inject.Inject

@InjectViewState
class MenuPresenter
@Inject constructor()
    : MvpPresenter<MenuView>() {


}

