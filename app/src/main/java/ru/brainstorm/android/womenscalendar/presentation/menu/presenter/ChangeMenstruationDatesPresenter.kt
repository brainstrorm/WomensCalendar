package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import android.content.Context
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.domain.predictor.PredictorImpl
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.differenceBetweenDates
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.ChangeMenstruationDatesFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ChangeMenstruationDatesView
import javax.inject.Inject

@InjectViewState
class ChangeMenstruationDatesPresenter
@Inject
constructor()
    : MvpPresenter<ChangeMenstruationDatesView>(){

    @Inject
    lateinit var cycleDao: CycleDao

    @Inject
    lateinit var predictorImpl: PredictorImpl

    fun save(startDate : LocalDate?, averageDurationOfMenstruation : Int?, fm : FragmentManager, context: Context,
             fragment : Fragment){
        if (startDate != null && averageDurationOfMenstruation != null) {
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val cycle = cycleDao.getAll()[0]
                    val length = cycleDao.getAll().size
                    cycle.lengthOfMenstruation = averageDurationOfMenstruation
                    cycle.predicted = false
                    for(cycle in cycleDao.getAll()){
                        cycleDao.delete(cycle)
                    }
                    cycleDao.insert(cycle)
                    predictorImpl.predict(length-1, PreferenceManager.getDefaultSharedPreferences(context))
                }
                job.join()
            }
        }
    }
}