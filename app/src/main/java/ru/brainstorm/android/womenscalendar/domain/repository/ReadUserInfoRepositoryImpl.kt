package ru.brainstorm.android.womenscalendar.domain.repository

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.data.User
import ru.brainstorm.android.womenscalendar.data.repository.ReadUserInfoRepository
import ru.brainstorm.android.womenscalendar.domain.repository.SaveQuizAnswersRepositoryImpl.Companion.BIRTH_DATE
import ru.brainstorm.android.womenscalendar.domain.repository.SaveQuizAnswersRepositoryImpl.Companion.FIREBASE_ID

class ReadUserInfoRepositoryImpl(private val sharedPreferences: SharedPreferences) : ReadUserInfoRepository{

    override suspend fun readUserInfoAsync() = GlobalScope.launch(Dispatchers.IO) {
        val firebaseId = sharedPreferences.getString(FIREBASE_ID, "")
        if (firebaseId?.isNotEmpty() == true)
            User.firebaseId = firebaseId
        val birthDate = sharedPreferences.getString(BIRTH_DATE, "")
        if (birthDate?.isNotEmpty() == true)
            User.birthDate = birthDate
    }
}