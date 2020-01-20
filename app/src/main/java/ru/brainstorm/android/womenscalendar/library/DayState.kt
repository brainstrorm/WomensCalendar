package ru.brainstorm.android.womenscalendar.library

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 18.01.2020
 */
enum class DayState {
    NONE,
    START_MENSTRUATION_NOW,
    END_MENSTRUATION_NOW,
    CURRENT_MENSTRUATION,
    MENSTRUATION,
    OVULATION,
    EXACT_OVULATION
}