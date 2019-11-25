package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.*
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import java.util.*
import javax.inject.Inject

@InjectViewState
class MenuPresenter
@Inject constructor()
    : MvpPresenter<MenuView>() {

    private var stackOfFragments =  Stack<String>()
    internal var date = ""
    internal var text = ""

    fun providePart(part : String){
        viewState.setPart(part)
    }

    fun addFragmentToBackStack(fragment: AbstractMenuFragment){
        stackOfFragments.push(fragment.getPart())
    }

    fun popBackStack(fm : FragmentManager){
        if(!stackOfFragments.isEmpty()){
            setFragment(fm, stackOfFragments.pop())
        }
    }
    fun setFragment(fm: FragmentManager, part : String){
        lateinit var fragment : AbstractMenuFragment
        for (fr in fm.fragments) {
            fm.beginTransaction()
                .hide(fr)
                .commit()
        }
        when(part){
            "calendar" ->{
                if(fm.findFragmentByTag(CalendarPickerFragment.TAG) == null) {
                    fragment = CalendarPickerFragment()
                    fragment.apply {
                        fm.beginTransaction()
                            .add(R.id.for_fragment, this, CalendarPickerFragment.TAG)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }else{
                    fragment = fm.findFragmentByTag(CalendarPickerFragment.TAG) as AbstractMenuFragment
                    fragment.apply {
                        fm.beginTransaction()
                            .show(this)
                            .commit()
                        var fr = fm.findFragmentByTag(SelectedDayNoteFragment.TAG)
                        if(fr != null) {
                            (fr as SelectedDayNoteFragment).selectedDayNotePresenter.setInformationFromCalendar(fr.getDate())
                            fm.beginTransaction()
                                .show(fm.findFragmentByTag(SelectedDayNoteFragment.TAG)!!)
                                .commit()
                        }
                        viewState.setPart(this.getPart())
                    }
                }
            }
            "today" ->{
                if(fm.findFragmentByTag(WeekModeCalendarFragment.TAG) == null) {
                    fragment = WeekModeCalendarFragment()
                    fragment.apply {
                        fm.beginTransaction()
                            .add(R.id.for_fragment, this, WeekModeCalendarFragment.TAG)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }else{
                    fragment = fm.findFragmentByTag(WeekModeCalendarFragment.TAG) as AbstractMenuFragment
                    fragment.apply {
                        fm.beginTransaction()
                            .show(this)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }
            }
            "notes" ->{
                if(fm.findFragmentByTag(ListOfNotesFragment.TAG) == null) {
                    fragment = ListOfNotesFragment()
                    fragment.apply {
                        fm.beginTransaction()
                            .add(R.id.for_fragment, this, ListOfNotesFragment.TAG)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }else{
                    fragment = fm.findFragmentByTag(ListOfNotesFragment.TAG) as AbstractMenuFragment
                    fragment.apply {
                        fm.beginTransaction()
                            .show(this)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }
            }
            "note_redactor" -> {
                if(fm.findFragmentByTag(ListOfNotesFragment.TAG) != null) {
                    fm.beginTransaction()
                        .remove(fm.findFragmentByTag(ListOfNotesFragment.TAG)!!)
                        .commit()
                }
                if(stackOfFragments.peek() == "calendar") {
                    val fragment_ =
                        fm.findFragmentByTag(SelectedDayNoteFragment.TAG) as SelectedDayNoteFragment
                    date = fragment_.getDate()
                    text = fragment_.getText()
                }
                if(fm.findFragmentByTag(NoteRedactorFragment.TAG) == null) {
                    fragment = NoteRedactorFragment()
                    fragment.provideInformation(date, text)
                    fragment.apply {
                        fm.beginTransaction()
                            .add(R.id.for_fragment, this, NoteRedactorFragment.TAG)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }else{
                    fragment = fm.findFragmentByTag(NoteRedactorFragment.TAG) as NoteRedactorFragment
                    fragment.provideInformation(date, text)
                    fragment.noteRedactorPresenter.viewState.setInformation()
                    fragment.apply {
                        fm.beginTransaction()
                            .show(this)
                            .commit()
                        viewState.setPart(this.getPart())
                    }
                }
            }
            "change_menstruation_dates" -> {
                fragment = ChangeMenstruationDatesFragment();
                fragment.apply {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, this, ChangeMenstruationDatesFragment.TAG)
                        .commit()
                }
            }
        }
    }
}

