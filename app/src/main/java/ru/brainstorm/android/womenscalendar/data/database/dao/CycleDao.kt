package ru.brainstorm.android.womenscalendar.data.database.dao

import androidx.room.*
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 26.10.2019
 */
@Dao
interface CycleDao {
    @Query("SELECT * FROM cycle")
    suspend fun getAll(): List<Cycle>

    @Query("SELECT * FROM cycle WHERE startOfCycle = :startOfCycle")
    suspend fun getByStart(startOfCycle: String): Cycle

    @Insert
    suspend fun insert(cycle: Cycle)

    @Update
    suspend fun update(cycle: Cycle)

    @Delete
    suspend fun delete(cycle: Cycle)
}