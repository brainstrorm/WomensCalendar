package ru.brainstorm.android.womenscalendar.domain.predictor

import android.content.SharedPreferences
import kotlinx.coroutines.Job

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 27.10.2019
 */
interface Predictor {
    /**
     * @see Job
     * @param count - count of cycles Impl should predict
     * @return Job - launch return, to callback, that computation is completed
     */
    suspend fun predict(count: Int, pref : SharedPreferences): Job

}