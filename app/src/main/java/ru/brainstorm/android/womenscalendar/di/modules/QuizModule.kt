package ru.brainstorm.android.womenscalendar.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.brainstorm.android.womenscalendar.data.repository.SaveQuizAnswersRepository
import ru.brainstorm.android.womenscalendar.domain.repository.ReadQuizAnswersRepositoryImpl
import ru.brainstorm.android.womenscalendar.domain.repository.SaveQuizAnswersRepositoryImpl
import javax.inject.Singleton

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 15.10.2019
 */
@Module
class QuizModule(private val application: Application) {

    companion object {
        private const val FIRST_LAUNCH = "women.data.first.launch"
    }

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context)
            : SharedPreferences = context.getSharedPreferences(FIRST_LAUNCH, Context.MODE_PRIVATE)

    @Provides
    fun provideReadQuizAnswerRepository(sharedPreferences: SharedPreferences)
            : ReadQuizAnswersRepositoryImpl = ReadQuizAnswersRepositoryImpl(sharedPreferences)

    @Provides
    fun provideSaveQuizAnswerRepository(sharedPreferences: SharedPreferences)
            : SaveQuizAnswersRepositoryImpl = SaveQuizAnswersRepositoryImpl(sharedPreferences)
}