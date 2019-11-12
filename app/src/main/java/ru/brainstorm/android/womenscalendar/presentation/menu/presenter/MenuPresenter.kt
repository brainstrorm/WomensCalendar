package ru.brainstorm.android.womenscalendar.presentation.menu.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.AbstractMenuFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.ListOfNotesFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import javax.inject.Inject

@InjectViewState
class MenuPresenter
@Inject constructor()
    : MvpPresenter<MenuView>() {


    fun providePart(part : String){
        viewState.setPart(part)
    }

    fun setFragment(fm: FragmentManager, part : String){
        lateinit var fragment : AbstractMenuFragment
        when(part){
            "calendar" ->{
                fragment = CalendarPickerFragment()
            }
            "today" ->{
                fragment = WeekModeCalendarFragment()
            }
            "notes" ->{
                fragment = ListOfNotesFragment()
            }
        }
        fragment.apply {
            fm.beginTransaction()
            .replace(R.id.for_fragment, this)
            .commit()
            viewState.setPart(this.getPart())
        }
    }
}

