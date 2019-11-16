package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.CalendarPickerPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.CalendarPickerView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.*
import javax.inject.Inject


class CalendarPickerFragment : AbstractMenuFragment(), CalendarPickerView{

    companion object{
        val TAG = "CalendarPicker"
    }
    @InjectPresenter
    lateinit var calendarPickerPresenter: CalendarPickerPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().calendarPickerPresenter()

    @Inject
    lateinit var cycleDao: CycleDao

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

        App.appComponent.inject(this)
        var menstruationDays = listOf<Cycle>()
        GlobalScope.async(Dispatchers.IO){
            menstruationDays = cycleDao.getAll()
            return@async menstruationDays
        }

        var calendarView = view.findViewById<com.kizitonwose.calendarview.CalendarView>(R.id.calendarView)
        weekDays.put("Mon", "Пн")
        weekDays.put("Tue", "Вт")
        weekDays.put("Wed", "Ср")
        weekDays.put("Thu", "Чт")
        weekDays.put("Fri", "Пт")
        weekDays.put("Sat", "Сб")
        weekDays.put("Sun", "Вс")

        months.put("October", "Октябрь")
        months.put("November", "Ноябрь")
        months.put("December", "Декабрь")
        months.put("January", "Январь")
        months.put("February", "Февраль")
        months.put("March", "Март")
        months.put("April", "Апрель")
        months.put("May", "Май")
        months.put("June", "Июнь")
        months.put("July", "Июль")
        months.put("August", "Август")
        months.put("September", "Сентябрь")

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
                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()
                startBlobe.makeInVisible()
                endBlobe.makeInVisible()
                todayRound.makeInVisible()
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
                        when(day.date){
                            in startMenstruation..endMenstruation -> {
                                //textView.setTextColorRes(R.color.colorPinkSelected)
                                if(day.date == startMenstruation){
                                    textView.background = startBackground
                                    startBlobe.isVisible = true

                                }
                                if(day.date == endMenstruation){
                                    textView.background = endBackground
                                    endBlobe.isVisible = true
                                }
                                if(startMenstruation < day.date && day.date < endMenstruation){
                                    textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                                }
                            }
                            in startOvulation..endOvulation -> {
                                if(day.date == ovulationDate){
                                    textView.setBackgroundResource(R.drawable.ovulation_round_selected)
                                    textView.setTextColorRes(R.color.color_White)
                                }else {
                                    textView.setTextColorRes(R.color.colorOfChosenNumberOrange)
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
                    if(day.date == today)
                        todayRound.isVisible = true
                    if(day.date == selectedDate)
                        textView.setTextSize(25F)
                    //выделение по нажатию
                    /*if (day.date.isBefore(today)) {
                        textView.setTextColorRes(R.color.colorPrimaryDark)
                    } else {
                        when {
                            startDate == day.date && endDate == null -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.example_4_single_selected_bg)
                            }
                            day.date == startDate -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                updateDrawableRadius(textView)
                                textView.background = startBackground
                            }
                            startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                            }
                            day.date == endDate -> {
                                textView.setTextColorRes(R.color.colorPrimaryDark)
                                updateDrawableRadius(textView)
                                textView.background = endBackground
                            }
                            day.date == today -> {
                                textView.setTextColorRes(R.color.example_4_grey)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.example_4_today_bg)
                            }
                            else -> {
                                textView.setTextColorRes(R.color.example_4_grey)
                            }
                        }
                    }*/
                } /*else {

                    // This part is to make the coloured selection background continuous
                    // on the blank in and out dates across various months and also on dates(months)
                    // between the start and end dates if the selection spans across multiple months.

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
                            textView.setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle)
                        }
                    }
                }*/
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

        //For save button!!! or ContinueButton
        /*exFourSaveButton.setOnClickListener click@{
            val startDate = startDate
            val endDate = endDate
            if (startDate != null && endDate != null) {
                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
                val text = "Selected: ${formatter.format(startDate)} - ${formatter.format(endDate)}"
                Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(requireView(), "No selection. Searching all Airbnb listings.", Snackbar.LENGTH_LONG)
                    .show()
            }
            fragmentManager?.popBackStack()
        }*/


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


}
