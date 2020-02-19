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
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.domain.predictor.PredictorImpl
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.Interval
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
             fragment : Fragment, intervals: MutableList<Interval>){
        if (startDate != null && averageDurationOfMenstruation != null) {
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {

                    val real_cycles : MutableList<Cycle> = mutableListOf()

                    for( i in 0..intervals.size-2) {

                        if (intervals[i].isChanged){

                            // Check the null !!!
                            val real_cycle = Cycle()
                            real_cycle.startOfCycle = intervals[i].startOfCycle.toString()
                            real_cycle.lengthOfMenstruation = differenceBetweenDates(intervals[i].startOfCycle,intervals[i].endOfCycle)
                            real_cycle.lengthOfCycle = differenceBetweenDates(intervals[i].startOfCycle,intervals[i+1].startOfCycle)-1
                            real_cycle.ovulation =  java.time.LocalDate.parse(real_cycle.startOfCycle).plusDays(real_cycle.lengthOfCycle-14.toLong()).toString()
                            real_cycle.predicted = false

                            real_cycles.add(real_cycle)
                        }

                    }

                    if (intervals[intervals.size-1].isChanged){
                        val real_cycle = Cycle()
                        real_cycle.startOfCycle = intervals[intervals.size-1].startOfCycle.toString()
                        real_cycle.lengthOfMenstruation = differenceBetweenDates(intervals[intervals.size-1].startOfCycle,intervals[intervals.size-1].endOfCycle)
                        real_cycle.lengthOfCycle = 28
                        real_cycle.ovulation =  java.time.LocalDate.parse(real_cycle.startOfCycle).plusDays(real_cycle.lengthOfCycle-14.toLong()).toString()
                        real_cycle.predicted = false

                        real_cycles.add(real_cycle)
                    }


                    val length = cycleDao.getAll().size

                    if (real_cycles.isEmpty()) {
                        val cycle = cycleDao.getAll()[0]
                        cycle.lengthOfMenstruation = averageDurationOfMenstruation
                        cycle.predicted = false
                        real_cycles.add(cycle)
                    }

                    for(cycle in cycleDao.getAll()){
                        cycleDao.delete(cycle)
                    }

                    for (cycle in real_cycles){
                        cycleDao.insert(cycle)
                    }

                    predictorImpl.predict(length-real_cycles.size, PreferenceManager.getDefaultSharedPreferences(context))
                }
                job.join()
            }
        }
    }
}