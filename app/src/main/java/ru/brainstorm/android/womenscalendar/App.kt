package ru.brainstorm.android.womenscalendar

import android.app.Application
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.brainstorm.android.womenscalendar.data.database.AppDatabase
import ru.brainstorm.android.womenscalendar.di.AppComponent
import ru.brainstorm.android.womenscalendar.di.DaggerAppComponent
import ru.brainstorm.android.womenscalendar.di.modules.QuizModule


class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
        initializeDatabase()
        AndroidThreeTen.init(this)
    }

    private fun initializeDatabase() {
        appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "database").build()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder()
            .quizModule(QuizModule(this))
            .build()
    }


}