package ru.brainstorm.android.womenscalendar.library

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.gridlayout.widget.GridLayout
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
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

    var adapter: YearAdapter = YearAdapter(Year.of(1970), App.appDatabase.cycleDao(), App.appDatabase.noteDao())

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
            val currentMonthNotes = adapter.notesList.filter { it.first.monthValue == i }.map { it.second }
            val currentMonthCycles = adapter.cycleList.filter { YearMonth.parse(it.startOfCycle, adapter.dateFormatter) == month || startedBefore(it, month) }
            (gridLayout[i - 1] as MonthView).adapter.also {
                it.noteList = currentMonthNotes
                it.cycleList = currentMonthCycles
                it.month = month
            }
            (gridLayout[i-1] as MonthView).setOnClickListener {
                (context as MenuActivity).apply {
                    val calendarMonthFragment = supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG) as CalendarPickerFragment
                    calendarMonthFragment.calendarView.scrollToMonth(org.threeten.bp.YearMonth.parse("${month.toString()}"))
                        if(btnMonthOrYearChecked == 1) {
                            btnMonthOrYear.setImageResource(R.drawable.ic_toggle_button_second)
                            txtvwMonth.setTextColorRes(R.color.grey_for_year)
                            txtvwYear.setTextColorRes(R.color.white)
                            menuPresenter.setFragment(supportFragmentManager, "calendar_year_mode")
                            btnMonthOrYearChecked = 2
                        }else{
                            btnMonthOrYear.setImageResource(R.drawable.ic_toggle_button)
                            txtvwMonth.setTextColorRes(R.color.white)
                            txtvwYear.setTextColorRes(R.color.grey_for_year)
                            menuPresenter.setFragment(supportFragmentManager, "calendar")
                            btnMonthOrYearChecked = 1
                        }
                }
            }
        }
    }

    private fun startedBefore(it: Cycle, month: YearMonth): Boolean {
        val start = LocalDate.parse(it.startOfCycle, adapter.dateFormatter)
        val end = start.plusDays(it.lengthOfCycle.toLong())
        return (start.monthValue <= month.monthValue && month.monthValue <= end.monthValue)
    }

    open inner class YearAdapter(year: Year, private val cycleDao: CycleDao, private val noteDao: NoteDao) {

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var notesList: List<Pair<YearMonth, Note>> = emptyList()

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
                notesList = noteDao.getAll()
                    .map { Pair(YearMonth.parse(it.noteDate, dateFormatter), it) }
                    .filter { it.first.year == year.value }
                cycleList = cycleDao.getAll().filter { Year.parse(it.startOfCycle, dateFormatter).value == year.value || startedBefore(it) }
            }
        }

        private fun startedBefore(cycle: Cycle): Boolean {
            val ld = LocalDate.parse(cycle.startOfCycle, dateFormatter).plusDays(cycle.lengthOfCycle.toLong())
            return ld.year == year.value
        }
    }
}