package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import androidx.fragment.app.Fragment
import moxy.MvpAppCompatFragment

abstract class AbstractMenuFragment : MvpAppCompatFragment() {

    abstract fun getPart() : String
}