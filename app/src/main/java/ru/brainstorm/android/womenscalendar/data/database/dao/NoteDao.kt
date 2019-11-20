package ru.brainstorm.android.womenscalendar.data.database.dao

import androidx.room.*
import ru.brainstorm.android.womenscalendar.data.database.entities.Note

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 26.10.2019
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE noteDate = :noteDate")
    suspend fun getByDate(noteDate: String): Note?

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getById(id: Long): Note

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}