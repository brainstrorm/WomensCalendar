package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.data.User
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.repository.SaveQuizAnswersRepository

class SaveQuizAnswersRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val cycleDao: CycleDao)
        : SaveQuizAnswersRepository {

    companion object{
        const val FIREBASE_ID = "current.fire.base.id"
        const val BIRTH_DATE = "current.birth.date"
    }

    override suspend fun saveInfo(cycle: Cycle) = GlobalScope.launch(Dispatchers.IO) {
        sharedPreferences.edit{
            clear()
            putString(FIREBASE_ID, User.firebaseId)
            putString(BIRTH_DATE, User.birthDate)
            apply()
        }
        cycleDao.insert(cycle)
    }


}