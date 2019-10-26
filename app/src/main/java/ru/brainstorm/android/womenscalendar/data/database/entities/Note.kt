package ru.brainstorm.android.womenscalendar.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 26.10.2019
 */
@Entity
class Note {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    lateinit var noteDate: String

    lateinit var noteText: String
}