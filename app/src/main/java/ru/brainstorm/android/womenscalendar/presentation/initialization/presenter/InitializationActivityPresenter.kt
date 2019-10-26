package ru.brainstorm.android.womenscalendar.presentation.initialization.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.presentation.initialization.view.InitializationActivityView
import javax.inject.Inject

@InjectViewState
class InitializationActivityPresenter
    @Inject constructor()
            : MvpPresenter<InitializationActivityView>()
{
    fun waiting() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(3_000)
            viewState.goToMenu()
        }

    }
}