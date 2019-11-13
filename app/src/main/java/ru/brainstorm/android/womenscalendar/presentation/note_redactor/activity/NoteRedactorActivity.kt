package ru.brainstorm.android.womenscalendar.presentation.note_redactor.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity

class NoteRedactorActivity : AppCompatActivity() {

    companion object {

        const val TAG = "NoteRedactor"

        fun provideIntent(packageContext: Context) = Intent(packageContext, NoteRedactorActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_redactor)

        supportActionBar?.hide()
    }
}
