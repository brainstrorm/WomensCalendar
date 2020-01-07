package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.calendar_day_legend.view.*
import kotlinx.android.synthetic.main.calendar_header.view.*
import kotlinx.coroutines.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.dao.NoteDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.data.database.entities.Note
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.CalendarPickerPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.CalendarPickerView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class CalendarPickerFragment : AbstractMenuFragment(), CalendarPickerView{

    private lateinit var pref : SharedPreferences

    companion object{
        val TAG = "CalendarPicker"
    }
    @InjectPresenter
    lateinit var calendarPickerPresenter: CalendarPickerPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().calendarPickerPresenter()

    @Inject
    lateinit var cycleDao: CycleDao

    @Inject
    lateinit var noteDao: NoteDao

    private val today = LocalDate.now()

    private var weekDays = HashMap<String, String>()
    private var months = HashMap<String, String>()

    //private var startDate: LocalDate? = null
    //private var endDate: LocalDate? = null
    private var selectedDate: LocalDate? = null

    private val headerDateFormatter = DateTimeFormatter.ofPattern("EEE'\n'd MMM")

    private val startBackground: GradientDrawable by lazy {
        return@lazy requireContext().getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start)!! as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        return@lazy requireContext().getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end)!! as GradientDrawable
    }

    private var radiusUpdated = false

    /**
     * We set the radius of the continuous selection background drawable dynamically
     * since the view size is `match parent` hence we cannot determine the appropriate
     * radius value which would equal half of the view's size beforehand.
     */
    private fun updateDrawableRadius(textView: TextView) {
        if (radiusUpdated) return
        radiusUpdated = true
        val radius = (textView.height / 2).toFloat()
        startBackground.setCornerRadius(topLeft = radius, bottomLeft = radius)
        endBackground.setCornerRadius(topRight = radius, bottomRight = radius)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_calendar_picker, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        App.appComponent.inject(this)
        var menstruationDays = listOf<Cycle>()
        var notes = listOf<Note>()
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                menstruationDays = cycleDao.getAll()
                notes = noteDao.getAll()
            }
            job.join()
        }

        var noteDates = HashMap<String, String>()

        for(note in notes){
            noteDates.put(note.noteDate, note.noteDate)
        }


        var calendarView = view.findViewById<com.kizitonwose.calendarview.CalendarView>(R.id.calendarView)

        updateLocale()

        val daysOfWeek = daysOfWeekFromLocale()

        val currentMonth = YearMonth.now()
        calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), daysOfWeek.first())
        calendarView.scrollToMonth(currentMonth)


        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.findViewById<TextView>(R.id.calendarDayText)
            val roundBgView = view.findViewById<View>(R.id.exFourRoundBgView)
            val blobeStart = view.findViewById<ImageView>(R.id.blobeStart)
            val blobeEnd = view.findViewById<ImageView>(R.id.blobeEnd)
            val todayRound = view.findViewById<ImageView>(R.id.todayRound)
            val todayRing = view.findViewById<ImageView>(R.id.for_circle)

            //обработчик нажатий
            init {
                view.setOnClickListener {
                    if(day.owner == DayOwner.THIS_MONTH) {
                        selectedDate = day.date
                        calendarPickerPresenter.addNoteFragment(fragmentManager!!, day.date)
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
                val roundBgView = container.roundBgView
                val startBlobe = container.blobeStart
                val endBlobe = container.blobeEnd
                val todayRound = container.todayRound
                val todayRing = container.todayRing
                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()
                startBlobe.makeInVisible()
                endBlobe.makeInVisible()
                todayRound.makeInVisible()
                todayRing.makeInVisible()
                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()
                    textView.setTextSize(16F)
                    textView.setTextColorRes(R.color.example_4_grey)
                    for (days in menstruationDays){
                        val startMenstruation = LocalDate.parse(days.startOfCycle)
                        val endMenstruation = LocalDate.parse(days.startOfCycle).plusDays(days.lengthOfMenstruation.toLong())
                        val ovulationDate = LocalDate.parse(days.ovulation)
                        val startOvulation = LocalDate.parse(days.ovulation).minusDays(4)
                        val endOvulation = LocalDate.parse(days.ovulation).plusDays(4)
                        if(day.date >= startMenstruation && day.date <= startMenstruation.plusDays(days.lengthOfCycle.toLong())) {
                            when (day.date) {
                                in startMenstruation..endMenstruation -> {
                                    //textView.setTextColorRes(R.color.colorPinkSelected)
                                    if (day.date == startMenstruation) {
                                        textView.background = startBackground
                                        startBlobe.isVisible = true

                                    }
                                    if (day.date == endMenstruation) {
                                        textView.background = endBackground
                                        endBlobe.isVisible = true
                                    }
                                    if (startMenstruation < day.date && day.date < endMenstruation) {
                                        textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                                    }
                                    if (day.date == today)
                                        todayRing.setBackgroundResource(R.drawable.red_circle)
                                }
                                in startOvulation..endOvulation -> {
                                    if (day.date == ovulationDate) {
                                        if (day.date == today) {
                                            textView.setBackgroundResource(R.drawable.ovulation_round_selected)
                                            textView.setTextColorRes(R.color.color_White)
                                        } else {
                                            textView.setBackgroundResource(R.drawable.orange_round)
                                            textView.setTextColorRes(R.color.color_White)
                                        }
                                    } else {
                                        if (day.date == today) {
                                            textView.setBackgroundResource(R.drawable.orange_circle)
                                        }
                                        textView.setTextColorRes(R.color.colorOfChosenNumberOrange)
                                    }
                                }
                                else -> {
                                    if (day.date == today)
                                        todayRing.setBackgroundResource(R.drawable.black_circle)
                                }
                            }
                        }
                        if (startMenstruation != null && endMenstruation != null) {
                            // Mimic selection of inDates that are less than the startDate.
                            // Example: When 26 Feb 2019 is startDate and 5 Mar 2019 is endDate,
                            // this makes the inDates in Mar 2019 for 24 & 25 Feb 2019 look selected.
                            if ((day.owner == DayOwner.PREVIOUS_MONTH
                                        && startMenstruation.monthValue == day.date.monthValue
                                        && endMenstruation.monthValue != day.date.monthValue) ||
                                // Mimic selection of outDates that are greater than the endDate.
                                // Example: When 25 Apr 2019 is startDate and 2 May 2019 is endDate,
                                // this makes the outDates in Apr 2019 for 3 & 4 May 2019 look selected.
                                (day.owner == DayOwner.NEXT_MONTH
                                        && startMenstruation.monthValue != day.date.monthValue
                                        && endMenstruation.monthValue == day.date.monthValue) ||

                                // Mimic selection of in and out dates of intermediate
                                // months if the selection spans across multiple months.
                                (startMenstruation < day.date && endMenstruation > day.date
                                        && startMenstruation.monthValue != day.date.monthValue
                                        && endMenstruation.monthValue != day.date.monthValue)
                            ) {
                                    textView.background = null
                            }
                        }
                    }
                    if(day.date == today) {
                        todayRing.isVisible = true
                    }
                    if(day.date == selectedDate)
                        textView.setTextSize(25F)

                    if(noteDates.containsKey(day.date.toString()))
                        todayRound.makeVisible()
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
                val monthTitle = "${months[month.yearMonth.month.name.toLowerCase().capitalize()]} ${month.year}"
                container.textView.text = monthTitle
            }
        }


    }



    override fun onStart() {
        super.onStart()
        val closeIndicator = requireContext().getDrawableCompat(R.drawable.ic_close)?.apply {
            setColorFilter(requireContext().getColorCompat(R.color.example_4_grey), PorterDuff.Mode.SRC_ATOP)
        }
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(closeIndicator)
        requireActivity().window.apply {
            // Update statusbar color to match toolbar color.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarColor = requireContext().getColorCompat(R.color.color_White)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                statusBarColor = Color.GRAY
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.apply {
            // Reset statusbar color.
            statusBarColor = requireContext().getColorCompat(R.color.colorPrimaryDark)
            decorView.systemUiVisibility = 0
        }
    }

    override fun getPart(): String = "calendar"


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

}
