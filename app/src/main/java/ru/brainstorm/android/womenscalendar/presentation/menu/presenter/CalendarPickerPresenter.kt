package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import androidx.fragment.app.FragmentManager
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.SelectedDayNoteFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.CalendarPickerView
import javax.inject.Inject


@InjectViewState
class CalendarPickerPresenter
    @Inject
    constructor()
            : MvpPresenter<CalendarPickerView>() {
        fun addNote(fm: FragmentManager){
            val fragment = SelectedDayNoteFragment()
            fragment.apply {
                fm.beginTransaction()
                    .add(R.id.for_notes, this)
                    .commit()
            }
        }
}

