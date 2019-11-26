package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.differenceBetweenDates
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ChangeMenstruationDatesView
import java.time.LocalDate
import javax.inject.Inject

@InjectViewState
class ChangeMenstruationDatesPresenter
@Inject
constructor()
    : MvpPresenter<ChangeMenstruationDatesView>(){

    @Inject
    lateinit var cycleDao: CycleDao

    fun save(startDate : LocalDate, endDate : LocalDate, fm : FragmentManager){
        if (startDate != null && endDate != null) {
            runBlocking {
                val job = GlobalScope.launch(Dispatchers.IO) {
                    val cycle = cycleDao.getById(1)
                    cycle.startOfCycle = startDate.toString()
                    cycle.lengthOfMenstruation = differenceBetweenDates(startDate, endDate)
                    for(cycle in cycleDao.getAll()){
                        cycleDao.delete(cycle)
                    }
                    cycleDao.insert(cycle)
                }
                job.join()
            }
            if (fm.findFragmentByTag(CalendarPickerFragment.TAG) != null) {
                fm.beginTransaction()
                    .remove(fm.findFragmentByTag(CalendarPickerFragment.TAG)!!)
                    .commit()
            }
            if(fm.findFragmentByTag(WeekModeCalendarFragment.TAG) != null){
                fm.beginTransaction()
                    .remove(fm.findFragmentByTag(WeekModeCalendarFragment.TAG)!!)
                    .commit()
            }
        }
    }
}