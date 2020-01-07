package ru.brainstorm.android.womenscalendar.presentation.statistics.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.progressview.ProgressView
import kotlinx.android.synthetic.main.activity_statistic.*
import kotlinx.coroutines.*
import org.w3c.dom.Text
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class StatisticsActivity : AppCompatActivity() {

    companion object {

        const val TAG = "Statistics"

        fun provideIntent(packageContext: Context) = Intent(packageContext, StatisticsActivity::class.java)
    }

    private lateinit var pref : SharedPreferences

    private lateinit var txtvwDurationOfCycleAndMenstruation : TextView
    private lateinit var txtvwAvgLengthOfCycle : TextView
    private lateinit var txtvwAvgLengthOfMenstruation : TextView
    private lateinit var txtvwPressDatesOfNewMenstruation : TextView

    @Inject
    lateinit var cycleDao: CycleDao

    class Adapter(private val cycles: List<Cycle>): RecyclerView.Adapter<Adapter.ViewHolder>(){
        private val dateFormatter = DateTimeFormatter.ofPattern("dd")
        private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
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
                holder?.daysOfCycle?.setText("Текущий цикл: ${dateFormatter.format(startOfCycle)} " +
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        supportActionBar?.hide()

        pref = PreferenceManager.getDefaultSharedPreferences(this)

        updateLocale()

        txtvwAvgLengthOfCycle = findViewById<TextView>(R.id.Avg_length_of_cycle)
        txtvwAvgLengthOfMenstruation = findViewById(R.id.avg_length_of_menstruation)
        txtvwDurationOfCycleAndMenstruation = findViewById(R.id.duration_of_cycle_and_menstruation)
        txtvwPressDatesOfNewMenstruation = findViewById(R.id.press_dates_of_new_menstruation)

        //get data from db
        App.appComponent.inject(this)
        var cycles = listOf<Cycle>()
        GlobalScope.async(Dispatchers.IO){
            cycles = cycleDao.getAll()
            val text = cycles[0].startOfCycle
            recyclerView = findViewById(R.id.setOfStatictics)
            recyclerView.layoutManager = LinearLayoutManager(this@StatisticsActivity)
            recyclerView.adapter = Adapter(cycles)
            return@async cycles
        }


    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwAvgLengthOfCycle = findViewById<TextView>(R.id.Avg_length_of_cycle)
        txtvwAvgLengthOfMenstruation = findViewById(R.id.avg_length_of_menstruation)
        txtvwDurationOfCycleAndMenstruation = findViewById(R.id.duration_of_cycle_and_menstruation)
        txtvwPressDatesOfNewMenstruation = findViewById(R.id.press_dates_of_new_menstruation)

        txtvwDurationOfCycleAndMenstruation.setText(R.string.text_duration_of_menstruation)
        txtvwAvgLengthOfMenstruation.setText(R.string.text_avg_length_of_menstruation)
        txtvwAvgLengthOfCycle.setText(R.string.text_avg_length_of_cycle)
        txtvwPressDatesOfNewMenstruation.setText(R.string.text_press_last_menstruation)
    }


}
