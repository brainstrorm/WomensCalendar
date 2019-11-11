package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.progressview.ProgressView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import javax.inject.Inject

class ListOfNotesFragment : Fragment() {

    @Inject
    lateinit var noteDao: NoteDao

    private lateinit var recyclerView : RecyclerView
    private lateinit var notes : List<Note>
    inner class Adapter(private val notes: List<Note>): RecyclerView.Adapter<Adapter.ViewHolder>(){
        override fun getItemCount() = notes.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_note_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val note = notes[position]
            val noteText = note.noteText
            val noteDate = note.noteDate

            holder.textOfNote?.setText(noteText)
            holder.dateOfNote?.setText(noteDate)

            holder.itemView.setOnClickListener(){
                //TODO
            }
        }

        inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
            var textOfNote: TextView? = null
            var dateOfNote: TextView? = null

            init{
                textOfNote = itemView?.findViewById(R.id.text_of_note)
                dateOfNote = itemView?.findViewById(R.id.date_of_note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appComponent.inject(this)
        var note = Note()
        note.noteText = "text"
        note.noteDate = "11/11/2019"
        GlobalScope.async(Dispatchers.IO){
            noteDao.insert(note)
            notes = noteDao.getAll()
            return@async notes
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_of_notes, container, false)

        recyclerView = view.findViewById(R.id.notes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = Adapter(notes)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}