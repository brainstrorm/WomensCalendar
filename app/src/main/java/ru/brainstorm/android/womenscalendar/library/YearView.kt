package ru.brainstorm.android.womenscalendar.library

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.gridlayout.widget.GridLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * @project Calendar
 * @author Ilia Ilmenskii created on 12.01.2020
 */
class YearView(context: Context,
               attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    var adapter: YearAdapter = YearAdapter(Year.of(1970), App.appDatabase.cycleDao())

    private val mainView = View.inflate(context, R.layout.year_view, null)

    private val gridLayout = mainView.findViewById<GridLayout>(R.id.gridLayout_year)

    init {
        notifyDataChanged()
        addView(mainView)
    }

    private fun notifyDataChanged() {
        mainView.findViewById<TextView>(R.id.textView).text = adapter.year.value.toString()
        for (i in 1..12) {
            val month = adapter.year.atMonth(i)
            val currentMonthCycles = adapter.cycleList.filter { YearMonth.parse(it.startOfCycle, adapter.dateFormatter) == month || startedBefore(it, month) }
            (gridLayout[i - 1] as MonthView).adapter.also {
                it.cycleList = currentMonthCycles
                it.month = month
            }
        }
    }

    private fun startedBefore(it: Cycle, month: YearMonth): Boolean {
        val ld = LocalDate.parse(it.startOfCycle, adapter.dateFormatter).plusDays(it.lengthOfCycle.toLong())
        return ld.month == month.month
    }

    open inner class YearAdapter(year: Year, private val cycleDao: CycleDao) {

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var cycleList: List<Cycle> = emptyList()
        set(value) {
            field = value
            notifyDataChanged()
        }

        var year = year
        set(value) {
            field = value
            refilter()
        }

        private fun refilter() {
            GlobalScope.launch(Dispatchers.Main) {
                cycleDao.getAll().forEach { Log.d("CYCLE_FILTER", "${Year.parse(it.startOfCycle, dateFormatter).value == year.value}") }
                cycleList = cycleDao.getAll().filter { Year.parse(it.startOfCycle, dateFormatter).value == year.value || startedBefore(it) }
            }
        }

        private fun startedBefore(cycle: Cycle): Boolean {
            val ld = LocalDate.parse(cycle.startOfCycle, dateFormatter).plusDays(cycle.lengthOfCycle.toLong())
            return ld.year == year.value
        }

        fun<T : MonthView.MonthAdapter> setAdapterClazz(clazz: Class<T>, vararg parameters: Any) {
            try {
                val constructor = clazz.getConstructor(*parameters.map { it::class.java }.toTypedArray())
                for (i in 0..11) {
                    val newInstance = constructor.newInstance(parameters)
                    (gridLayout[i] as MonthView).adapter = newInstance
                }
                notifyDataChanged()
            } catch (e: NoSuchMethodException) {
                Log.d("YearView", "$e")
            } catch (e: SecurityException) {
                Log.d("YearView", "$e")
            }
        }
    }
}