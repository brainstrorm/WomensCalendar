package ru.brainstorm.android.womenscalendar.domain.predictor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 27.10.2019
 */
class PredictorImpl
    @Inject constructor(cycleDao: CycleDao)
        : Predictor {
    override suspend fun predict(count: Int): Job = GlobalScope.launch(Dispatchers.IO) {

    }
}