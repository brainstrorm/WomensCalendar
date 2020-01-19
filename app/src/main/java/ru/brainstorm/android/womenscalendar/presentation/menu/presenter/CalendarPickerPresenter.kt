package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import moxy.InjectViewState
import moxy.MvpPresenter
import org.threeten.bp.LocalDate
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.SelectedDayNoteFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.CalendarPickerView
import javax.inject.Inject


@InjectViewState
class CalendarPickerPresenter
    @Inject
    constructor()
            : MvpPresenter<CalendarPickerView>() {

        fun addNoteFragment(fm: FragmentManager, date: LocalDate){
            if(fm.findFragmentByTag(SelectedDayNoteFragment.TAG) == null) {
                val fragment = SelectedDayNoteFragment()
                fragment.apply {
                    fm.beginTransaction()
                        .add(R.id.for_notes, this, SelectedDayNoteFragment.TAG)
                        .commit()
                    this.provideDate(date.toString())
                }
            }else{
                val fragment = fm.findFragmentByTag(SelectedDayNoteFragment.TAG) as SelectedDayNoteFragment
                fragment.apply {
                    fragment.provideDate(date.toString())
                    selectedDayNotePresenter.setInformationFromCalendar(date.toString())
                    fm.beginTransaction()
                        .show(fm.findFragmentByTag(SelectedDayNoteFragment.TAG)!!)
                        .commit()
                }
            }
        }
        fun updateInformation(){

        }

}

