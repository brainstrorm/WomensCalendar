package ru.brainstorm.android.womenscalendar.di

import dagger.Component
import ru.brainstorm.android.womenscalendar.presentation.activity.SplashScreenActivity

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 14.10.2019
 */

@Component
interface AppComponent {
    /*
     * This component is the main DI component of application - it will inject
     * all activities\fragments and contexts
     */
    fun inject(activity: SplashScreenActivity)
}