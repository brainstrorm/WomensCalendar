package ru.brainstorm.android.womenscalendar.presentation.menu.extra

fun GetDayAddition(num: Int): String {
    val preLastDigit = num % 100 / 10
    if (preLastDigit == 1) {
        return "дней"
    }

    when (num % 10) {
        1 -> return "день"
        2, 3, 4 -> return "дня"
        else -> return "дней"
    }
}