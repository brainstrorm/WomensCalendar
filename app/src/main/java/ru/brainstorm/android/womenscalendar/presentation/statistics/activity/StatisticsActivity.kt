package ru.brainstorm.android.womenscalendar.presentation.statistics.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.time.LocalDate
import javax.inject.Inject

class StatisticsActivity : AppCompatActivity() {

    companion object {

        const val TAG = "Statistics"

        fun provideIntent(packageContext: Context) = Intent(packageContext, StatisticsActivity::class.java)
    }


    @Inject
    lateinit var cycleDao: CycleDao

    class Adapter(private val cycles: List<Cycle>): RecyclerView.Adapter<Adapter.ViewHolder>(){
        override fun getItemCount() = cycles.size
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentCycle = cycles[position]
            if(position == 0) {
                holder?.daysOfCycle?.text = Resources.getSystem().getString(R.string.current_cycle,
                    currentCycle.startOfCycle, LocalDate.parse(currentCycle.startOfCycle).
                        plusDays(currentCycle.lengthOfCycle.toLong()))
            }else{
                holder?.daysOfCycle?.text = Resources.getSystem().getString(R.string.cycle,
                    currentCycle.startOfCycle, LocalDate.parse(currentCycle.startOfCycle).
                        plusDays(currentCycle.lengthOfCycle.toLong()))
            }
            holder?.daysOfMenstruation?.text = Resources.getSystem().getString(
                R.string.length_of_menstruation, currentCycle.lengthOfMenstruation)
            holder?.lengthOfCycle?.text = Resources.getSystem().getString(R.string.length_of_cycle,
                currentCycle.lengthOfCycle)
            holder?.progressBar?.progress = currentCycle.lengthOfMenstruation/currentCycle.lengthOfCycle
        }

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
            var daysOfCycle: TextView? = null
            var daysOfMenstruation: TextView? = null
            var lengthOfCycle: TextView? = null
            var progressBar: ProgressBar? = null
            init{
                daysOfCycle = itemView?.findViewById(R.id.days_of_cycle)
                daysOfMenstruation = itemView?.findViewById(R.id.days_of_menstruation)
                lengthOfCycle = itemView?.findViewById(R.id.length_of_cycle)
                progressBar = itemView?.findViewById(R.id.progressBar)
            }
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        //get data from db
        App.appComponent.inject(this)
        var cycles = listOf<Cycle>()
        GlobalScope.async(Dispatchers.IO){
            cycles = cycleDao.getAll()
            return@async cycles
        }

        recyclerView = findViewById(R.id.setOfStatictics)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(cycles)

    }

}
