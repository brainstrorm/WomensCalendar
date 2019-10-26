package ru.brainstorm.android.womenscalendar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.database.entities.Note

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 26.10.2019
 */
@Database(entities = [Note::class, Cycle::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun cycleDao(): CycleDao
}