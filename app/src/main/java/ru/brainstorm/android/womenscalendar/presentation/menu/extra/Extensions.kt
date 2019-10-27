package ru.brainstorm.android.womenscalendar.presentation.menu.extra

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