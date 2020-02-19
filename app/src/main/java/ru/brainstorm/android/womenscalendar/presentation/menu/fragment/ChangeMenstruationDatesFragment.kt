package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.calendar_day_legend.view.*
import kotlinx.android.synthetic.main.calendar_header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import org.w3c.dom.Text
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.Interval
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.differenceBetweenDates
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.ChangeMenstruationDatesPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.ChangeMenstruationDatesView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.daysOfWeekFromLocale
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.makeInVisible
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.makeVisible
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round


class ChangeMenstruationDatesFragment : AbstractMenuFragment(), ChangeMenstruationDatesView {



    @Inject
    lateinit var cycleDao: CycleDao

    private lateinit var pref : SharedPreferences

    @InjectPresenter
    lateinit var changeMenstruationDatesPresenter: ChangeMenstruationDatesPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().changeMenstruationDatesPresenter()

    companion object{
        val TAG = "ChangeMenstruationDates"
    }

    private var startDate : LocalDate? = null
    private var endDate : LocalDate? = null
    var intervals : MutableList<Interval> = mutableListOf()
    override fun getPart(): String = "change_menstruation_dates"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        App.appComponent.inject(this)
        return inflater.inflate(R.layout.fragment_change_menstruation_dates, container, false)
    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private var weekDays = HashMap<String, String>()
    private var months = HashMap<String, String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        var cycles = listOf<Cycle>()
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                cycles = cycleDao.getAll()
                for(cycle in cycles){
                    val startOfCycle = LocalDate.parse(cycle.startOfCycle)
                    val endOfCycle = LocalDate.parse(cycle.startOfCycle).plusDays((cycle.lengthOfMenstruation-1).toLong())
                    if(endOfCycle.isBefore(LocalDate.now()) || endOfCycle.isEqual(LocalDate.now())
                        || (startOfCycle.isBefore(LocalDate.now()) && endOfCycle.isAfter(LocalDate.now()))){
                        intervals.add(Interval(startOfCycle, endOfCycle, true))
                    }
                }
            }
            job.join()
        }
        updateLocale()

        var calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        val daysOfWeek = daysOfWeekFromLocale()

        val currentMonth = YearMonth.now()
        calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = view.findViewById<TextView>(R.id.calendarDayText)
            val roundField = view.findViewById<View>(R.id.exFourRoundBgView)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        val date = day.date
                        /*if (startDate != null) {
                            if (date < startDate || endDate != null) {
                                startDate = date
                                endDate = null
                            } else if (date != startDate) {
                                endDate = date
                                intervals.add(Pair(startDate, endDate))

                                activity!!.apply {
                                    val fragment = supportFragmentManager.findFragmentByTag(TAG)!!
                                    supportFragmentManager.beginTransaction()
                                        .detach(fragment)
                                        .attach(fragment)
                                        .commit()
                                }
                            }
                        } else {
                            startDate = date
                        }*/
                        when {
                            isStartOfCycle(date) != null -> {
                                val i = isStartOfCycle(date)!!
                                intervals[i].isChanged = true
                                intervals[i].startOfCycle = intervals[i].startOfCycle.plusDays(1)
                            }
                            isEndOfCycle(date) != null -> {
                                val i = isEndOfCycle(date)!!
                                intervals[i].isChanged = true
                                intervals[i].endOfCycle = intervals[i].endOfCycle.minusDays(1)
                            }
                            isInTheAcceptableRangeBeforeStart(date) != null -> {
                                val i = isInTheAcceptableRangeBeforeStart(date)!!
                                intervals[i].isChanged = true
                                intervals[i].startOfCycle = date
                            }
                            isInTheAcceptableRangeAfterEnd(date) != null -> {
                                val i = isInTheAcceptableRangeAfterEnd(date)!!
                                intervals[i].isChanged = true
                                intervals[i].endOfCycle = date
                            }
                        }
                        calendarView.notifyCalendarChanged()
                    }
                }
            }

        }
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val roundField = container.roundField

                textView.text = null
                textView.background = null
                roundField.makeInVisible()
                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.text = day.date.dayOfMonth.toString()
                        when {
                            startDate == day.date && endDate == null -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_selected)
                            }
                            day.date == startDate -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_selected)
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_selected)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_selected)
                            }
                            day.date == today -> {
                                textView.setTextColorRes(R.color.color_red)
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_not_selected)
                            }
                            else -> {
                                textView.setTextColorRes(R.color.colorDays)
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_not_selected)
                            }
                        }

                    } else {
                        // This part is to make the coloured selection background continuous
                        // on the blank in and out dates across various months and also on dates(months)
                        // between the start and end dates if the selection spans across multiple months.
                        //roundField.makeInVisible()
                        val startDate = startDate
                        val endDate = endDate
                        if (startDate != null && endDate != null) {
                            // Mimic selection of inDates that are less than the startDate.
                            // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                            // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                            if ((day.owner == DayOwner.PREVIOUS_MONTH
                                        && startDate.monthValue == day.date.monthValue
                                        && endDate.monthValue != day.date.monthValue) ||
                                // Mimic selection of outDates that are greater than the endDate.
                                // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                                // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                                (day.owner == DayOwner.NEXT_MONTH
                                        && startDate.monthValue != day.date.monthValue
                                        && endDate.monthValue == day.date.monthValue) ||

                                // Mimic selection of in and out dates of intermediate
                                // months if the selection spans across multiple months.
                                (startDate < day.date && endDate > day.date
                                        && startDate.monthValue != day.date.monthValue
                                        && endDate.monthValue != day.date.monthValue)
                            ) {
                                roundField.setBackgroundResource(R.drawable.round_field_selected)
                            }
                        }
                    }
                if (day.owner == DayOwner.THIS_MONTH) {
                    if(!intervals.isEmpty()){
                        for(interval in intervals){
                            if(day.date.isEqual(interval.startOfCycle) || day.date.isEqual(interval.endOfCycle) ||
                                (day.date.isAfter(interval.startOfCycle) && day.date.isBefore(interval.endOfCycle))){
                                roundField.makeVisible()
                                roundField.setBackgroundResource(R.drawable.round_field_selected)
                            }
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exFourHeaderText
            val legendLayout = view.legendLayout.children.forEachIndexed { index, view ->
                (view as TextView).apply {
                    text = weekDays[daysOfWeek[index].name.take(3).toLowerCase().capitalize()]!!
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                    setTextColorRes(R.color.colorDaysOfWeek)
                }
            }
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                container.textView.text = "${months[month.yearMonth.month.name.toLowerCase().capitalize()]!!} ${month.year}"
            }
        }

        (activity as MenuActivity).apply {
            findViewById<TextView>(R.id.btn_save).setOnClickListener {
                changeMenstruationDatesPresenter.save(
                    getStartDate(),
                    getAverageDurationOfMenstruation(),
                    supportFragmentManager,
                    context!!,
                    this@ChangeMenstruationDatesFragment,intervals
                )
                val calendarMonth = supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)
                val calendarYear = supportFragmentManager.findFragmentByTag(CalendarYearModeFragment.TAG)
                val weekMode = supportFragmentManager.findFragmentByTag(WeekModeCalendarFragment.TAG)
                val statistic = supportFragmentManager.findFragmentByTag(StatisticsFragment.TAG)
                if (calendarMonth != null) {
                    supportFragmentManager.beginTransaction()
                        .detach(calendarMonth)
                        .attach(calendarMonth)
                        .remove(this@ChangeMenstruationDatesFragment)
                        .add(R.id.for_fragment, ChangeMenstruationDatesFragment(), TAG)
                        .commitNow()
                }
                if (calendarYear != null) {
                    supportFragmentManager.beginTransaction()
                        .detach(calendarYear)
                        .attach(calendarYear)
                        .commitNow()
                }
                if (weekMode != null){
                    supportFragmentManager.beginTransaction()
                        .detach(weekMode)
                        .attach(weekMode)
                        .commitNow()
                }
                if (statistic != null){
                    supportFragmentManager.beginTransaction()
                        .detach(statistic)
                        .attach(statistic)
                        .commitNow()
                }
                intervals.clear()
                menuPresenter.popBackStack(supportFragmentManager)

            }
            findViewById<TextView>(R.id.btn_canceled).setOnClickListener {
                /*intervals.clear()
                supportFragmentManager.beginTransaction()
                    .remove(this@ChangeMenstruationDatesFragment)
                    .add(R.id.for_fragment, ChangeMenstruationDatesFragment(), TAG)
                    .commitNow()*/
                if (!intervals.isEmpty()) {
                    intervals.remove(intervals.last())
                    startDate = null
                    endDate = null
                    calendarView.notifyCalendarChanged()
                }
            }
        }
    }

    fun getStartDate() : LocalDate?{
        return intervals[0].startOfCycle
    }

    fun getEndDate() : LocalDate? {
        return intervals[0].endOfCycle
    }

    fun getAverageDurationOfMenstruation() : Int?{
        val cyclDate: CycleDao
        var averageDurationOfMenstruation = 0
        var sum_intervals = 0
        var count = 0

        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {


        if (!intervals.isEmpty()){
            for(interval in intervals) {
                if (interval.isChanged) {
                    sum_intervals += differenceBetweenDates(
                        interval.startOfCycle!!,
                        interval.endOfCycle!!
                    )
                    count++
                }
            }

            if(count != 0) {
                averageDurationOfMenstruation = (sum_intervals) / (count)
            }
            else {
                averageDurationOfMenstruation = cycleDao.getAll()[0].lengthOfMenstruation
            }
        }else{
            averageDurationOfMenstruation = 0
        }
            }
            job.join()
        }

        return averageDurationOfMenstruation
    }

    fun updateLocale(){

        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        months.put("October", resources.getString(R.string.october))
        months.put("November", resources.getString(R.string.november))
        months.put("December", resources.getString(R.string.december))
        months.put("January", resources.getString(R.string.january))
        months.put("February", resources.getString(R.string.february))
        months.put("March", resources.getString(R.string.march))
        months.put("April", resources.getString(R.string.april))
        months.put("May", resources.getString(R.string.may))
        months.put("June", resources.getString(R.string.june))
        months.put("July", resources.getString(R.string.july))
        months.put("August", resources.getString(R.string.august))
        months.put("September", resources.getString(R.string.september))

        weekDays.put("Mon", resources.getString(R.string.monday))
        weekDays.put("Tue", resources.getString(R.string.tuesday))
        weekDays.put("Wed", resources.getString(R.string.wednesday))
        weekDays.put("Thu", resources.getString(R.string.thursday))
        weekDays.put("Fri", resources.getString(R.string.friday))
        weekDays.put("Sat", resources.getString(R.string.saturday))
        weekDays.put("Sun", resources.getString(R.string.sunday))
    }

    fun isStartOfCycle(date : LocalDate) : Int?{
        for(i in intervals.indices){
            val lengthOfInterval = differenceBetweenDates(intervals[i].startOfCycle!!,
                intervals[i].endOfCycle!!)
            if(date.isEqual(intervals[i].startOfCycle) && lengthOfInterval >= 2)
                return i
        }
        return null
    }

    fun isEndOfCycle(date : LocalDate) : Int?{
        for(i in intervals.indices){
            val lengthOfInterval = differenceBetweenDates(intervals[i].startOfCycle!!,
                intervals[i].endOfCycle!!)
            if(date.isEqual(intervals[i].endOfCycle) && lengthOfInterval >= 2)
                return i
        }
        return null
    }
    fun isInTheAcceptableRangeBeforeStart(date : LocalDate) : Int?{
        for(i in intervals.indices){
            val range = 9 - differenceBetweenDates(intervals[i].startOfCycle!!,
                intervals[i].endOfCycle!!).toLong()
            val rangeDate = intervals[i].startOfCycle!!.minusDays(if (range > 0) range else 0)
            if(date.isBefore(intervals[i].startOfCycle) && (date.isAfter(rangeDate) || date.isEqual(rangeDate)))
                return i
        }
        return null
    }
    fun isInTheAcceptableRangeAfterEnd(date : LocalDate) : Int?{
        for(i in intervals.indices){
            val range = 9 - differenceBetweenDates(intervals[i].startOfCycle!!,
                intervals[i].endOfCycle!!).toLong()
            val rangeDate = intervals[i].endOfCycle!!.plusDays(if (range > 0) range else 0)
            if(date.isAfter(intervals[i].endOfCycle) && (date.isBefore(rangeDate) || date.isEqual(rangeDate)))
                return i
        }
        return null
    }
}
