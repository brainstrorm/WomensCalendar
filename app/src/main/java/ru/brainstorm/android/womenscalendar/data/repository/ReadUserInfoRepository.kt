package ru.brainstorm.android.womenscalendar.data.repository

import kotlinx.coroutines.Job

interface ReadUserInfoRepository{
    suspend fun readUserInfoAsync() : Job
}