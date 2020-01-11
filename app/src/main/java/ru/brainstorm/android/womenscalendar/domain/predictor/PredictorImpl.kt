package ru.brainstorm.android.womenscalendar.domain.predictor

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.NotificationsFragment
import java.security.AccessController.getContext
import java.time.LocalDate
import java.time.format.DateTimeParseException
import javax.inject.Inject


class PredictorImpl
    @Inject constructor(val cycleDao: CycleDao)
        : Predictor {
    private lateinit var pref : SharedPreferences

    override suspend fun predict(count: Int, pref: SharedPreferences): Job = GlobalScope.launch(Dispatchers.IO) {


        val set_inject: List<Cycle> = cycleDao.getAll()
        val setLengthofmenstruation: MutableList<Int> = mutableListOf()
        val setLengthofcycle: MutableList<Int> = mutableListOf()
        val setOvulations: MutableList<String> = mutableListOf()


        if(set_inject.size == 1) {

                if((set_inject[0].lengthOfCycle < 21)&&(set_inject[0].lengthOfCycle > 35)){
                    set_inject[0].lengthOfCycle = 24
                }

                if((set_inject[0].lengthOfMenstruation < 2)&&(set_inject[0].lengthOfMenstruation > 8)){
                    set_inject[0].lengthOfMenstruation = 5
                }
                    set_inject[0].ovulation = LocalDate.parse(set_inject.last().startOfCycle)
                        .plusDays(set_inject[0].lengthOfCycle - 14.toLong()).toString()
                }

        for (cycle in set_inject) {
            setLengthofmenstruation.add(cycle.lengthOfMenstruation)
            setLengthofcycle.add(cycle.lengthOfCycle)
            setOvulations.add(cycle.ovulation)
        }

        val avgSetLengthOfMenstruation = setLengthofmenstruation.average().toLong()
        val avgSetLengthOfCycle = setLengthofcycle.average().toLong()

       var set_update =  set_inject.toMutableList()

        for(i in 1..count){

                var firstDayOfNewCycle: String =
                    LocalDate.parse(set_update.last().startOfCycle).plusDays(avgSetLengthOfCycle)
                        .toString()
                val ovulation = LocalDate.parse(firstDayOfNewCycle).plusDays(avgSetLengthOfCycle-14.toLong()).toString()

                var newCycle = Cycle()
                newCycle.startOfCycle = firstDayOfNewCycle
                newCycle.ovulation = ovulation
                newCycle.lengthOfCycle = avgSetLengthOfCycle.toInt()
                newCycle.lengthOfMenstruation = avgSetLengthOfMenstruation.toInt()
                newCycle.predicted = true

                set_update.add(newCycle)
                cycleDao.insert(newCycle)

        }

        set_inject[0].predicted = true
        cycleDao.update(set_inject[0])

        if (isCheckedStartMenstruation(pref)) {

            //<------> init our notification
            val notificationsFragment = NotificationsFragment()
            var notification = notificationsFragment.getNotification(notificationsFragment.resources.getString(R.string.notification_menstruation_start))



            //<------> create new alarm by method
            if (notification != null)
            notificationsFragment.scheduleNotification(org.threeten.bp.LocalDate.parse(FindDate(set_update).startOfCycle),CalculatePeriod(FindDate(set_update).lengthOfCycle))

        }

    }

    fun updateOvulation() = GlobalScope.launch(Dispatchers.IO) {
        val cycles = cycleDao.getAll()
        cycles.forEach {
            if (it.ovulation.isNotEmpty())
                return@forEach
            it.ovulation = LocalDate.parse(it.startOfCycle).plusDays((it.lengthOfMenstruation + 7).toLong()).toString()
            cycleDao.update(it)
        }
    }

    fun isCheckedStartMenstruation(pref: SharedPreferences):Boolean {
        return pref.getBoolean("StartMenstruation",false)
    }

    fun isCheckedEndMenstruation(pref: SharedPreferences):Boolean {
        return pref.getBoolean("EndMenstruation",false)
    }
    fun isCheckedOvulation(pref: SharedPreferences):Boolean {
        return pref.getBoolean("Ovulation",false)
    }
    fun isCheckedOpenFetilnost(pref: SharedPreferences):Boolean {
        return pref.getBoolean("OpenFetilnost",false)
    }
    fun isCheckedCloseFetilnost(pref: SharedPreferences):Boolean {
        return pref.getBoolean("CloseFetilnost",false)
    }


    fun FindDate(set_update: List<Cycle>): Cycle {
        val date = LocalDate.now()


        var ans = set_update.size-1

        for(i in 0..set_update.size-2) {

            if (date.compareTo(LocalDate.parse(set_update[i].startOfCycle)) <= 0) {
                if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle).plusDays(set_update[i].lengthOfCycle.toLong())) >= 0) {
                     ans = i+1
                }
            }
        }


        return set_update[ans]
    }

    fun CalculateDelay(startOfCycle: String):Int {
        val duringDate = LocalDate.now()
        val newDate = LocalDate.parse(startOfCycle)
        val newDays = newDate.dayOfYear - duringDate.dayOfYear

        return newDays*24*60*60*1000
    }

    fun CalculatePeriod(lengthOfCycle : Int):Int {
        return lengthOfCycle*24*60*60*1000
    }


}
