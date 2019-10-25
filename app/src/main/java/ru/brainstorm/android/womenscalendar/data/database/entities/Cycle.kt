package ru.brainstorm.android.womenscalendar.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 26.10.2019
 */
@Entity
class Cycle {
    @PrimaryKey
    lateinit var startOfCycle: String

    var lengthOfCycle: Int = 0

    var lengthOfMenstruation: Int = 0

    var predicted: Boolean = false
}