package ru.brainstorm.android.womenscalendar.library

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.getDrawableCompat
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * @project Calendar
 * @author Ilia Ilmenskii created on 29.12.2019
 */
class MonthView(context: Context,
                attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val mainView = View.inflate(context, R.layout.month_view, null)

    private val gridLayout = mainView.findViewById<GridLayout>(R.id.gridlayout_month)

    private val startBackground: GradientDrawable by lazy {
        return@lazy context.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start)!! as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        return@lazy context.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end)!! as GradientDrawable
    }

    var adapter: MonthAdapter = DefaultMonthAdapter(YearMonth.of(1970, Month.JANUARY))
        set(value) {
            field = value
            notifyDataChanged()
        }

    init {
        addView(mainView)
        notifyDataChanged()
    }

    private fun notifyDataChanged() {
        mainView.findViewById<TextView>(R.id.month_title).text = adapter.getTitle()
        gridLayout.removeAllViews()
        val rows = adapter.getRowCount()
        val cols = adapter.getColCount()
        gridLayout.rowCount = rows
        gridLayout.columnCount = cols
        for (i in 0..rows) {
            for (j in 0 until cols) {
                val view = adapter.bindDay(null, i, j)
                view ?: continue
                gridLayout.addView(view)
                view.apply {
                    val gridParams = layoutParams as GridLayout.LayoutParams
                    gridParams.setGravity(Gravity.CENTER)
                    gridParams.rowSpec = GridLayout.spec(i, 1f)
                    gridParams.columnSpec = GridLayout.spec(j, 1f)
//                    setPadding(resources.getDimension(R.dimen.line_padding).toInt(), 0, resources.getDimension(R.dimen.line_padding).toInt(), 0)
                }
            }
        }
    }

    interface MonthAdapter {
        var cycleList: List<Cycle>
        var month: YearMonth
        fun getRowCount(): Int
        fun getColCount(): Int
        fun getTitle(): String
        fun bindDay(parent: ViewGroup?, row: Int, col: Int): View?
    }

    open inner class DefaultMonthAdapter(month: YearMonth) : MonthAdapter {

        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        override var cycleList: List<Cycle> = emptyList()
        set(value) {
            field = value
            notifyDataChanged()
        }

        override var month: YearMonth = month
        set(value) {
            field = value
            notifyDataChanged()
        }

        private var offset: Int = 0

        override fun getTitle(): String = month.month.name

        override fun getColCount(): Int = 7

        override fun getRowCount(): Int {
            offset = month.atDay(1).dayOfWeek.value - 1
            val atDayStart = getColCount() - month.atDay(1).dayOfWeek.value + 1
            val forOtherRows = month.lengthOfMonth() - atDayStart
            var addition = 1
            if (forOtherRows % getColCount() != 0) {
                addition = 2
            }
            return forOtherRows / getColCount() + addition
        }

        override fun bindDay(parent: ViewGroup?, row: Int, col: Int): View? {
            val date = calculateDate(row, col)
            if (date < 0)
                return null
            val tv = TextView(context)
            tv.gravity = Gravity.CENTER
            tv.text = date.toString()
            tv.textSize = 10f
            if (isMenstruationDay(date)) {
                if(isStartOfMenstruation(date)){
                    tv.background = startBackground
                }else if(isEndOfMenstruation(date)){
                    tv.background = endBackground
                }else{
                    tv.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                }
            }
            if (isOvulationDay(date)) {
                if(isDirectOvulationDay(date)){
                    tv.setBackgroundResource(R.drawable.orange_round_for_year)
                    tv.setTextColorRes(R.color.color_White)
                }else{
                    tv.setTextColorRes(R.color.colorOfChosenNumberOrange)
                }
            }

            return tv
        }

        private fun isOvulationDay(day: Int): Boolean {
            cycleList.forEach {
                val start = LocalDate.parse(it.ovulation, dateFormatter).minusDays(4L)
                val end = start.plusDays(8L)
                val curDay = LocalDate.of(month.year, month.month, day)
                if (curDay.isAfter(start) and curDay.isBefore(end) or curDay.isEqual(start) or curDay.isEqual(end))
                    return true
            }
            return false
        }

        private fun isDirectOvulationDay(day: Int): Boolean {
            cycleList.forEach {
                val start = LocalDate.parse(it.ovulation, dateFormatter).minusDays(4L)
                val end = start.plusDays(8L)
                val curDay = LocalDate.of(month.year, month.month, day)
                if (curDay.equals(LocalDate.parse(it.ovulation, dateFormatter)))
                    return true
            }
            return false
        }

        private fun isMenstruationDay(day: Int): Boolean {
            cycleList.forEach {
                val start = LocalDate.parse(it.startOfCycle, dateFormatter)
                val end = start.plusDays(it.lengthOfMenstruation.toLong())
                val curDay = LocalDate.of(month.year, month.month, day)
                if (curDay.isAfter(start) and curDay.isBefore(end) or curDay.isEqual(start) or curDay.isEqual(end))
                    return true
            }
            return false
        }

        private fun isStartOfMenstruation(day : Int): Boolean {
            cycleList.forEach {
                val start = LocalDate.parse(it.startOfCycle, dateFormatter)
                val end = start.plusDays(it.lengthOfMenstruation.toLong())
                val curDay = LocalDate.of(month.year, month.month, day)
                if (curDay.equals(start))
                    return true
            }
            return false
        }

        private fun isEndOfMenstruation(day : Int): Boolean {
            cycleList.forEach {
                val start = LocalDate.parse(it.startOfCycle, dateFormatter)
                val end = start.plusDays(it.lengthOfMenstruation.toLong())
                val curDay = LocalDate.of(month.year, month.month, day)
                if (curDay.equals(end))
                    return true
            }
            return false
        }
        protected fun calculateDate(row: Int, col: Int): Int {
            val date  = (row*getColCount() + col - offset + 1)
            if (date > 0 && date <= month.lengthOfMonth()) {
                return date
            }
            return -1
        }
    }
}