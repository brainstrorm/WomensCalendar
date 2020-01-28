package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.progressview.ProgressView
import kotlinx.coroutines.*
import ru.brainstorm.android.womenscalendar.App

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.getDayAddition
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class StatisticsFragment : AbstractMenuFragment() {

    companion object {

        const val TAG = "Statistics"

    }


    private lateinit var pref : SharedPreferences

    private lateinit var txtvwDurationOfCycleAndMenstruation : TextView
    private lateinit var txtvwAvgLengthOfCycle : TextView
    private lateinit var txtvwAvgLengthOfMenstruation : TextView
    private lateinit var txtvwPressDatesOfNewMenstruation : TextView
    private lateinit var txtvwAvgOfMenstruation : TextView
    private lateinit var txtvwAvgOfCycles : TextView
    private lateinit var arrow : ImageView



    @Inject
    lateinit var cycleDao: CycleDao

    class Adapter(private val cycles: List<Cycle>, context: Context): RecyclerView.Adapter<Adapter.ViewHolder>(){
        private val dateFormatter = DateTimeFormatter.ofPattern("dd")
        private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
        private val A_context = context
        override fun getItemCount() = cycles.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentCycle = cycles[position]
            var startOfCycle = LocalDate.parse(currentCycle.startOfCycle)
            var endOfCycle = LocalDate.parse(currentCycle.startOfCycle).
                plusDays(currentCycle.lengthOfCycle.toLong())
            if(position == 0) {

                holder?.daysOfCycle?.setText(A_context!!.resources.getString(R.string.during_cycle)+" "+"${dateFormatter.format(startOfCycle)} " +
                        "${monthFormatter.format(startOfCycle)} - ${dateFormatter.format(endOfCycle)}" +
                        " ${monthFormatter.format(endOfCycle)}")
            }else{
                holder?.daysOfCycle?.setText("${dateFormatter.format(startOfCycle)} " +
                        "${monthFormatter.format(startOfCycle)} - ${dateFormatter.format(endOfCycle)}" +
                        " ${monthFormatter.format(endOfCycle)}")
            }
            holder?.daysOfMenstruation?.setText("${currentCycle.lengthOfMenstruation}")
            holder?.lengthOfCycle?.setText("${currentCycle.lengthOfCycle}")
            holder?.progressBar?.progress=currentCycle.lengthOfMenstruation.toFloat()/currentCycle.lengthOfCycle.toFloat()*100
        }

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
            var daysOfCycle: TextView? = null
            var daysOfMenstruation: TextView? = null
            var lengthOfCycle: TextView? = null
            var progressBar: ProgressView? = null

            init{
                daysOfCycle = itemView?.findViewById(R.id.days_of_cycle)
                daysOfMenstruation = itemView?.findViewById(R.id.days_of_menstruation)
                lengthOfCycle = itemView?.findViewById(R.id.length_of_cycle)
                progressBar = itemView?.findViewById(R.id.progressBar)
            }
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun getPart(): String = "statistics"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        txtvwAvgLengthOfCycle = view.findViewById<TextView>(R.id.Avg_length_of_cycle)
        txtvwAvgLengthOfMenstruation = view.findViewById(R.id.avg_length_of_menstruation)
        //txtvwDurationOfCycleAndMenstruation = view.findViewById(R.id.duration_of_cycle_and_menstruation)
        //txtvwPressDatesOfNewMenstruation = view.findViewById(R.id.press_dates_of_new_menstruation)
        txtvwAvgOfCycles = view.findViewById<TextView>(R.id.days_of_cycle)
        txtvwAvgOfMenstruation = view.findViewById<TextView>(R.id.days_of_menstruation)
        //arrow = view.findViewById<ImageView>(R.id.arrow)

        updateLocale()



        //get data from db
        App.appComponent.inject(this)
        var cycles = listOf<Cycle>()
        GlobalScope.async(Dispatchers.IO){
            cycles = cycleDao.getAll()
            val text = cycles[0].startOfCycle
            recyclerView = view.findViewById(R.id.setOfStatictics)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = Adapter(cycles, context!!)
            return@async cycles
        }

        txtvwAvgOfCycles.setText(findAvg(cycleDao).first.toString()+" "+findAvg(cycleDao).first.toInt().getDayAddition(context!!))
        txtvwAvgOfMenstruation.setText(findAvg(cycleDao).second.toString()+" "+findAvg(cycleDao).second.toInt().getDayAddition(context!!))


        return view
    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        //txtvwAvgLengthOfCycle = view!!.findViewById<TextView>(R.id.Avg_length_of_cycle)
        //txtvwAvgLengthOfMenstruation = view!!.findViewById(R.id.avg_length_of_menstruation)
        //txtvwDurationOfCycleAndMenstruation = view!!.findViewById(R.id.duration_of_cycle_and_menstruation)
        //txtvwPressDatesOfNewMenstruation = view!!.findViewById(R.id.press_dates_of_new_menstruation)

        //txtvwDurationOfCycleAndMenstruation.setText(R.string.text_duration_of_menstruation)
        txtvwAvgLengthOfMenstruation.setText(R.string.text_avg_length_of_menstruation)
        txtvwAvgLengthOfCycle.setText(R.string.text_avg_length_of_cycle)
        //txtvwPressDatesOfNewMenstruation.setText(R.string.text_press_last_menstruation)
    }

    fun findAvg(cycleDao: CycleDao): Pair<Long,Long> {

        var menstruationDays = listOf<Cycle>()
        val setLengthofmenstruation: MutableList<Int> = mutableListOf()
        val setLengthofcycle: MutableList<Int> = mutableListOf()

        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                menstruationDays = cycleDao.getAll()
            }
            job.join()
        }

        menstruationDays.forEach{
            setLengthofmenstruation.add(it.lengthOfMenstruation)
            setLengthofcycle.add(it.lengthOfCycle)
        }

        val avgSetLengthOfMenstruation = setLengthofmenstruation.average().toLong()
        val avgSetLengthOfCycle = setLengthofcycle.average().toLong()

        return Pair(avgSetLengthOfCycle,avgSetLengthOfMenstruation)
    }
}
