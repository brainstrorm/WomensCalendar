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
            val fragment = stackOfFragments.pop()
            setFragment(fm, fragment)
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
                if(fm.findFragmentByTag(ChangeMenstruationDatesFragment.TAG) == null){
                    fragment = ChangeMenstruationDatesFragment()
                    fragment.apply {
                        fm.beginTransaction()
                            .add(R.id.for_fragment, this, ChangeMenstruationDatesFragment.TAG)
                            .commit()
                    }
                }else{
                    fragment = fm.findFragmentByTag(ChangeMenstruationDatesFragment.TAG)
                            as ChangeMenstruationDatesFragment
                    fragment.apply {
                        fm.beginTransaction()
                            .show(this)
                            .commit()
                    }
                }
                viewState.setPart("change_menstruation_dates")
            }
            "calendar_year_mode" -> {
                if(fm.findFragmentByTag(CalendarYearModeFragment.TAG) == null){
                    fragment = CalendarYearModeFragment()
                    fragment.apply {
                        fm.beginTransaction()
                            .add(R.id.for_fragment, this, CalendarYearModeFragment.TAG)
                            .commit()
                    }
                }else{
                    fragment = fm.findFragmentByTag(CalendarYearModeFragment.TAG)
                            as CalendarYearModeFragment
                    fragment.apply {
                        fm.beginTransaction()
                            .show(this)
                            .commit()
                    }
                    viewState.setPart("calendar_year_mode")
                }
            }
            "more" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, SettingsFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, SettingsFragment())
                        .commitNow()
                }
                viewState.setPart("more")
            }
            "languages" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, LanguagesFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, LanguagesFragment())
                        .commitNow()
                }
                viewState.setPart("languages")
            }
            "change_language" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, ChangeLanguageFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, ChangeLanguageFragment())
                        .commitNow()
                }
                viewState.setPart("change_language")
            }

            "notifications" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, NotificationsFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, NotificationsFragment())
                        .commitNow()
                }
                viewState.setPart("notifications")
            }

            "start_of_menstruation" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, MenstruationStartNotificationFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, MenstruationStartNotificationFragment())
                        .commitNow()
                }
                viewState.setPart("start_of_menstruation")
            }

            "end_of_menstruation" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, MenstruationEndNotificationFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, MenstruationEndNotificationFragment())
                        .commitNow()
                }
                viewState.setPart("end_of_menstruation")
            }
            "ovulation" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, OvulationNotificationFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, OvulationNotificationFragment())
                        .commitNow()
                }
                viewState.setPart("ovulation")
            }

            "opening_of_fertility_window" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, OpeningOfFertilityWindowNotificationFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, OpeningOfFertilityWindowNotificationFragment())
                        .commitNow()
                }
                viewState.setPart("opening_of_fertility_window")
            }

            "closing_of_fertility_window" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, ClosingOfFertilityWindowNotificationFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, ClosingOfFertilityWindowNotificationFragment())
                        .commitNow()
                }
                viewState.setPart("closing_of_fertility_window")
            }
            "about_app" -> {
                if (fm.findFragmentById(R.id.for_fragment) == null) {
                    fm.beginTransaction()
                        .add(R.id.for_fragment, AboutAppFragment())
                        .commitNow()
                } else {
                    fm.beginTransaction()
                        .replace(R.id.for_fragment, AboutAppFragment())
                        .commitNow()
                }
                viewState.setPart("about_app")
            }
        }
    }
}

