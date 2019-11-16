package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.brainstorm.android.womenscalendar.R

class NoteRedactorFragment : AbstractMenuFragment() {

    companion object{
        val TAG = "NoteRedactor"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_redactor, container, false)
    }

    override fun getPart(): String = "note_redactor"
}

