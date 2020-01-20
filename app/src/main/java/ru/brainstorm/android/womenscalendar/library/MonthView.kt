package ru.brainstorm.android.womenscalendar.library

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
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
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

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

        private val dateFormatSymbols = DateFormatSymbols()

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

        override fun getTitle(): String {
            return month.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
        }

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
            var view: View? = null
            val tv: TextView?
            val date = calculateDate(row, col)
            if (date < 0)
                return view
            val detected = detectDay(date)
            when (detected) {
                DayState.NONE -> {
                    view = TextView(context)
                    tv = view
                }
                DayState.START_MENSTRUATION_NOW -> {
                    view = inflate(context, R.layout.drop_day_item, null)
                    view.findViewById<View>(R.id.end_blobe).visibility = View.GONE
                    tv = view.findViewById(R.id.date)
                    tv.background = startBackground
                }
                DayState.END_MENSTRUATION_NOW -> {
                    view = inflate(context, R.layout.drop_day_item, null)
                    view.findViewById<View>(R.id.first_blobe).visibility = View.GONE
                    tv = view.findViewById(R.id.date)
                    tv.background = endBackground
                }
                DayState.CURRENT_MENSTRUATION -> {
                    view = inflate(context, R.layout.drop_day_item, null)
                    view.findViewById<View>(R.id.first_blobe).visibility = View.GONE
                    view.findViewById<View>(R.id.end_blobe).visibility = View.GONE
                    tv = view.findViewById(R.id.date)
                    view.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                }
                DayState.MENSTRUATION -> {
                    view = TextView(context)
                    tv = view
                    tv.setTextColorRes(R.color.settings_pink_btn)
                }
                DayState.OVULATION -> {
                    view = TextView(context)
                    tv = view
                    tv.setTextColorRes(R.color.settings_orange_btn)
                }
                DayState.EXACT_OVULATION -> {
                    view = TextView(context)
                    tv = view
                    tv.setBackgroundResource(R.drawable.orange_round_for_year)
                    tv.setTextColorRes(R.color.color_White)
                }
            }
            if (LocalDate.now() == LocalDate.of(month.year, month.month, date))
                roundToday(detected, tv)
            tv?.gravity = Gravity.CENTER
            tv?.text = date.toString()
            tv?.textSize = 10f
            return view
        }

        private fun roundToday(detected: DayState, tv: TextView?) {
            tv ?: return
            when(detected) {
                DayState.NONE -> tv.setBackgroundResource(R.drawable.black_circle_for_year)
                DayState.EXACT_OVULATION -> tv.setBackgroundResource(R.drawable.ovulation_round_selected_for_year)
                DayState.MENSTRUATION ->
                    tv.setBackgroundResource(R.drawable.red_circle_for_year)
                DayState.CURRENT_MENSTRUATION, DayState.START_MENSTRUATION_NOW, DayState.END_MENSTRUATION_NOW -> {
                    val back = tv.background
                    val circle = context.getDrawable(R.drawable.red_circle_for_year)
                    circle ?: return
                    val final = LayerDrawable(arrayOf(back, circle))
                    tv.background = final
                }
                DayState.OVULATION -> tv.setBackgroundResource(R.drawable.orange_circle_for_year)
            }
        }

        private fun detectDay(date: Int): DayState {
            val curDay = LocalDate.of(month.year, month.month, date)
            cycleList.forEach {
                val startOfMenstruation = LocalDate.parse(it.startOfCycle, dateFormatter)
                val endOfMenstruation = startOfMenstruation.plusDays(it.lengthOfMenstruation.toLong())
                if (LocalDate.now() in startOfMenstruation..startOfMenstruation.plusDays(it.lengthOfCycle.toLong())) {
                    when(curDay) {
                        startOfMenstruation -> return DayState.START_MENSTRUATION_NOW
                        endOfMenstruation -> return DayState.END_MENSTRUATION_NOW
                        else -> {
                            if (startOfMenstruation.isBefore(curDay) && endOfMenstruation.isAfter(curDay)) {
                                return DayState.CURRENT_MENSTRUATION
                            }
                        }
                    }
                }
                if (curDay in startOfMenstruation..endOfMenstruation) {
                    return DayState.MENSTRUATION
                }
                val exactOvulation = LocalDate.parse(it.ovulation, dateFormatter)
                if (exactOvulation == curDay)
                    return DayState.EXACT_OVULATION
                val startOfOvulation = exactOvulation.minusDays(4L)
                val endOfOvulation = exactOvulation.plusDays(4L)
                if (curDay in startOfOvulation..endOfOvulation)
                    return DayState.OVULATION
            }
            return DayState.NONE
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