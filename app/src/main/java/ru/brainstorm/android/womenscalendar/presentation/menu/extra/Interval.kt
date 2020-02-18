package ru.brainstorm.android.womenscalendar.presentation.menu.extra

import org.threeten.bp.LocalDate


data class Interval(var startOfCycle: LocalDate, var endOfCycle: LocalDate, var isChanged: Boolean)