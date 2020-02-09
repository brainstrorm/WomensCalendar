package ru.brainstorm.android.womenscalendar.presentation.menu.extra

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.widget.Toast
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment


fun Int.getDayAddition(context: Context): String {
    val pref = PreferenceManager.getDefaultSharedPreferences(context)
    when(pref.getString("language", "en")) {
        "ru" -> {
            val preLastDigit = this % 100 / 10
            if (preLastDigit == 1) {
                return context.resources.getString(R.string.days)
            }

            when (this % 10) {
                1 -> return context.resources.getString(R.string.day_1)
                2, 3, 4 -> return context.resources.getString(R.string.day_2_3_4)
                else -> return context.resources.getString(R.string.days)
            }
        }
        "en" -> {
            if (this == 1){
                return context.resources.getString(R.string.day_1)
            }else{
                return context.resources.getString(R.string.days)
            }
        }
        "es" -> {
            return context.resources.getString(R.string.day_1)
        }
        "pt" -> {
            if(this == 1) {
                return context.resources.getString(R.string.day_1)
            }else{
                return context.resources.getString(R.string.days)
            }
        }
    }
    return context.resources.getString(R.string.days)
}

fun differenceBetweenDates(startDate : LocalDate, endDate : LocalDate) : Int{
    if(startDate.year == endDate.year){
        return endDate.dayOfYear - startDate.dayOfYear
    }else{
        return endDate.dayOfYear + if (startDate.year % 4 == 0) 366 - startDate.dayOfYear
                else 365 - startDate.dayOfYear
    }
}

fun String.parseDate() : Pair<Int, Int>{
    lateinit var result : Pair<Int,Int>
    var time = this.split(":")
    result = Pair(time[0].toInt(), time[1].toInt())
    return result
}

fun CalculateDelay(startOfCycle: String):Int {
    val duringDate = java.time.LocalDate.now()
    val newDate = java.time.LocalDate.parse(startOfCycle)
    val newDays = newDate.dayOfYear - duringDate.dayOfYear

    return newDays*24*60*60*1000
}

fun CalculatePeriod(lengthOfCycle : Int):Int {
    return lengthOfCycle*24*60*60*1000
}


fun FindOvulation(set_update: List<Cycle>) : LocalDate {

    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 0..set_update.size-2) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].ovulation)) <= 0) {
            ans = i
            break
        }

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].ovulation)) > 0) {
            ans = i+1
            break
        }
    }

    return LocalDate.parse(set_update[ans].ovulation)
}

fun FindStartOfMenstruation(set_update: List<Cycle>) : LocalDate {
    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 0..set_update.size-1) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) > 0) {
            if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle).plusDays(set_update[i].lengthOfCycle.toLong()-1)) <= 0) {
                ans = i+1
            }
        }else if(date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) == 0){
            ans = i
        }
    }
    return LocalDate.parse(set_update[ans].startOfCycle)
}

fun FindEndOfMenstruation(set_update: List<Cycle>) : LocalDate {
    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 0..set_update.size-1) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) >= 0) {
            if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle).plusDays(set_update[i].lengthOfMenstruation.toLong()-1)) <= 0) {
                ans = i
            }else{
                ans = i+1
            }
        }
    }

    return LocalDate.parse(set_update[ans].startOfCycle).plusDays(set_update[ans].lengthOfMenstruation.toLong()-1)
}

fun FindOpenOfFertilnost(set_update: List<Cycle>) : LocalDate {

    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 0..set_update.size-1) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) >= 0) {
            if (date.compareTo(java.time.LocalDate.parse(set_update[i].ovulation).minusDays(6)) <= 0) {
                ans = i
            }else{
                ans = i+1
            }
        }
    }

    return LocalDate.parse(set_update[ans].ovulation).minusDays(6)

}

fun FindEndOfFertilnost(set_update: List<Cycle>) : LocalDate {

    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 0..set_update.size-1) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) >= 0) {
            if (date.compareTo(java.time.LocalDate.parse(set_update[i].ovulation).plusDays(1)) <= 0) {
                ans = i
            }else{
                ans = i+1
            }
        }
    }

    return LocalDate.parse(set_update[ans].ovulation).plusDays(1)
}


fun FindDate(set_update: List<Cycle>): Cycle {
    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 1..set_update.size-1) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) >= 0) {
            if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle).plusDays(set_update[i].lengthOfCycle.toLong())) <= 0) {
                ans = i-1
            }
        }
    }

    return set_update[ans]
}

fun FindCurrent(set_update: List<Cycle>): Cycle {
    val date = java.time.LocalDate.now()

    var ans = 0

    for(i in 1..set_update.size-1) {

        if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle)) >= 0) {
            if (date.compareTo(java.time.LocalDate.parse(set_update[i].startOfCycle).plusDays(set_update[i].lengthOfCycle.toLong())) <= 0) {
                ans = i
            }
        }
    }

    return set_update[ans]
}














