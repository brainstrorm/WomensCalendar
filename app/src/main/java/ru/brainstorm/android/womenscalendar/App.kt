package ru.brainstorm.android.womenscalendar

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.brainstorm.android.womenscalendar.di.AppComponent
import ru.brainstorm.android.womenscalendar.di.DaggerAppComponent
import ru.brainstorm.android.womenscalendar.di.modules.QuizModule

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 14.10.2019
 */
class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
        AndroidThreeTen.init(this)
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .quizModule(QuizModule(this))
            .build()
    }


}