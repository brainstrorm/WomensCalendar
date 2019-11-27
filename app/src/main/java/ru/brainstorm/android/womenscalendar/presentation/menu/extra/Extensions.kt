package ru.brainstorm.android.womenscalendar.presentation.menu.extra

import org.threeten.bp.LocalDate


fun Int.getDayAddition(): String {
    val preLastDigit = this % 100 / 10
    if (preLastDigit == 1) {
        return "дней"
    }

    when (this % 10) {
        1 -> return "день"
        2, 3, 4 -> return "дня"
        else -> return "дней"
    }
}

fun differenceBetweenDates(startDate : LocalDate, endDate : LocalDate) : Int{
    if(startDate.year == endDate.year){
        return endDate.dayOfYear - startDate.dayOfYear
    }else{
        return endDate.dayOfYear + if (startDate.year % 4 == 0) 366 - startDate.dayOfYear
                else 365 - startDate.dayOfYear
    }
}