package ru.brainstorm.android.womenscalendar.domain.predictor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import java.time.LocalDate
import javax.inject.Inject

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 27.10.2019
 */
class PredictorImpl
    @Inject constructor(val cycleDao: CycleDao)
        : Predictor {
    override suspend fun predict(count: Int): Job = GlobalScope.launch(Dispatchers.IO) {


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

                  set_inject[0].ovulation = LocalDate.parse(set_inject.last().startOfCycle).plusDays(set_inject[0].lengthOfCycle-14.toLong()).toString()
            }

        for (cycle in set_inject) {
            setLengthofmenstruation.add(cycle.lengthOfMenstruation)
            setLengthofcycle.add(cycle.lengthOfCycle)
            setOvulations.add(cycle.ovulation)
        }

        val avgSetLengthofmenstruation = setLengthofcycle.average().toLong()
        val avgSetLengthofcycle = setLengthofmenstruation.average().toLong()

       var set_update =  set_inject.toMutableList()

        for(i in 1..count){

            var firstDayOfNewCycle: String = LocalDate.parse(set_update.last().startOfCycle).plusDays(avgSetLengthofcycle).toString()

            val ovulation = LocalDate.parse(firstDayOfNewCycle).plusDays(avgSetLengthofcycle-14.toLong()).toString()

            val newCycle = Cycle(firstDayOfNewCycle,ovulation,avgSetLengthofcycle.toInt(),avgSetLengthofmenstruation.toInt())

            set_update.add(newCycle)
            cycleDao.insert(newCycle)
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
}

//count -> что делать ? добавляем ?
