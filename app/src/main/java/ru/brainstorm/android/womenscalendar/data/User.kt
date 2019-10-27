package ru.brainstorm.android.womenscalendar.data

/**
 * @project WomensCalendar
 * @author Ilia Ilmenskii created on 26.10.2019
 */
object User {
    lateinit var firebaseId: String

    lateinit var birthDate: String

    fun isInitialized(): Boolean = (this::firebaseId.isInitialized && this::birthDate.isInitialized)
}