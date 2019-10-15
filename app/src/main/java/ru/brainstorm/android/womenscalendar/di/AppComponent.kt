package ru.brainstorm.android.womenscalendar.di

import dagger.Component
import dagger.Subcomponent
import ru.brainstorm.android.womenscalendar.di.modules.QuizModule
import ru.brainstorm.android.womenscalendar.presentation.activity.SplashScreenActivity
import ru.brainstorm.android.womenscalendar.presentation.presenter.SplashScreenPresenter
import javax.inject.Singleton

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 14.10.2019
 */

@Component(modules = [QuizModule::class])
@Singleton
interface AppComponent {
    /*
     * This component is the main DI component of application - it will inject
     * all activities\fragments and contexts
     */
    fun inject(activity: SplashScreenActivity)

    fun presenter(): PresenterComponent

    @Subcomponent
    interface PresenterComponent {
        fun splashPresenter(): SplashScreenPresenter
    }
}