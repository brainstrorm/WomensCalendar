package ru.brainstorm.android.womenscalendar.di

import androidx.appcompat.view.menu.MenuPresenter
import dagger.Component
import dagger.Subcomponent
import ru.brainstorm.android.womenscalendar.di.modules.QuizModule
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.*
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.*
import ru.brainstorm.android.womenscalendar.presentation.quiz.presenter.*
import ru.brainstorm.android.womenscalendar.presentation.splash.activity.SplashScreenActivity
import ru.brainstorm.android.womenscalendar.presentation.splash.presenter.SplashScreenPresenter
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
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

    fun inject(fragment: SettingsFragment)

    fun inject(activity: SplashScreenActivity)

    fun inject(menu: MenuActivity)

    fun inject(calendarPicker : CalendarPickerFragment)


    fun inject(weekModeCalendar : WeekModeCalendarFragment)

    fun inject(statistics: StatisticsActivity)

    fun inject(listOfNotes: ListOfNotesFragment)

    fun inject(selectedDayNote: SelectedDayNoteFragment)

    fun inject(noteRedactor: NoteRedactorFragment)

    fun inject(changeMenstruationDates : ChangeMenstruationDatesFragment)

    fun inject(notificationsFragment: NotificationsFragment)

    fun inject(statisticsFragment: StatisticsFragment)

    fun presenter(): PresenterComponent

    @Subcomponent
    interface PresenterComponent {
        fun splashPresenter(): SplashScreenPresenter


        fun quizActivityPresenter(): QuizActivityPresenter

        fun calendarPickerForQuizPresenter(): CalendarPickerForQuizPresenter

        fun listOfNotesPresenter(): ListOfNotesPresenter

        fun menuPresenter() : ru.brainstorm.android.womenscalendar.presentation.menu.presenter.MenuPresenter

        fun calendarPickerPresenter() : CalendarPickerPresenter

        fun selectedDayNotePresenter() : SelectedDayNotePresenter

        fun noteRedactorPresenter() : NoteRedactorPresenter

        fun changeMenstruationDatesPresenter() : ChangeMenstruationDatesPresenter
    }
}