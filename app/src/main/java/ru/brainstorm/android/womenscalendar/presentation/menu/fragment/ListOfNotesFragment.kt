package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.di.DaggerAppComponent
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.sort
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.ListOfNotesPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ListOfNotesView
import javax.inject.Inject


class ListOfNotesFragment : AbstractMenuFragment(), ListOfNotesView {

    companion object{
        val TAG = "ListOfNotes"
    }
    @Inject
    lateinit var noteDao: NoteDao

    @InjectPresenter
    lateinit var fragmentPresenter: ListOfNotesPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().listOfNotesPresenter()

    private lateinit var recyclerView : RecyclerView
    private lateinit var notes : MutableList<Note>
    private lateinit var mAdView: AdView

    inner class Adapter(private val notes: List<Note>): RecyclerView.Adapter<Adapter.ViewHolder>(){
        override fun getItemCount() = notes.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_note_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val note = notes[position]
            var noteText = note.noteText
            if(noteText.length > 155){
                noteText = noteText.substring(0,155)+"..."
            }
            val noteDate = org.threeten.bp.LocalDate.parse(note.noteDate)

            holder.textOfNote?.setText(noteText)
            holder.dateOfNote?.setText("${noteDate.dayOfMonth}/${noteDate.monthValue}/${noteDate.year}")


            holder.itemView.setOnClickListener{
                val menuActivity = activity as MenuActivity
                menuActivity.menuPresenter.addFragmentToBackStack(this@ListOfNotesFragment)
                menuActivity.menuPresenter.date = note.noteDate
                menuActivity.menuPresenter.text = note.noteText
                menuActivity.menuPresenter.setFragment(menuActivity.supportFragmentManager, "note_redactor")
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_of_notes, container, false)

        App.appComponent.inject(this)

        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                notes = noteDao.getAll() as MutableList<Note>
                notes.sort(0, notes.size-1)
            }
            job.join()
        }

        recyclerView = view.findViewById(R.id.notes)
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = Adapter(notes)

        MobileAds.initialize(activity, "ca-app-pub-8660591775381486~3432926821")
        mAdView = view.findViewById(R.id.adView)
        val adRequest: AdRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun getPart(): String = "notes"

}
