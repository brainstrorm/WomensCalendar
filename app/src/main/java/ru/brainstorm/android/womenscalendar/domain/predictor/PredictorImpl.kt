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

        if(set_inject.size == 1) {

                if((set_inject[0].lengthOfCycle < 21)&&(set_inject[0].lengthOfCycle > 35)){
                    set_inject[0].lengthOfCycle = 24
                }

                if((set_inject[0].lengthOfMenstruation < 2)&&(set_inject[0].lengthOfMenstruation > 8)){
                    set_inject[0].lengthOfMenstruation = 5
                }

            }

        for (cycle in set_inject) {
            setLengthofmenstruation.add(cycle.lengthOfMenstruation)
            setLengthofcycle.add(cycle.lengthOfCycle)
        }

        val avgSetLengthofmenstruation = setLengthofcycle.average().toLong()
        val avgSetLengthofcycle = setLengthofmenstruation.average().toLong()

        var firstDayOfNewCycle: String = LocalDate.parse(set_inject.last().startOfCycle).plusDays(avgSetLengthofcycle).toString()


        for(i in 1..count){
            val newCycle:Cycle = Cycle(firstDayOfNewCycle,avgSetLengthofcycle.toInt(),avgSetLengthofmenstruation.toInt())
            cycleDao.insert(newCycle)
        }
    }
}

//count -> что делать ? добавляем ?
