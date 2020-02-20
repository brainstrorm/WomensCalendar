package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.calendar_day_layout_for_direcly_calendar.view.*
import kotlinx.android.synthetic.main.calendar_day_week_mode.view.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.*
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.domain.predictor.PredictorImpl
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.getDayAddition
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.PartOfCycle
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.differenceBetweenDates
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.CalendarPickerPresenter
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.getColorCompat
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
import javax.inject.Inject
import kotlin.time.days

class WeekModeCalendarFragment : AbstractMenuFragment() {

    companion object{
        val TAG = "WeekModeCalendar"
    }


    //@InjectPresenter
//    lateinit var calendarPickerPresenter: CalendarPickerPresenter

  //  @ProvidePresenter
   // fun providePresenter() = App.appComponent.presenter().calendarPickerPresenter()

    @Inject
    lateinit var cycleDao: CycleDao

    @Inject
    lateinit var predictorImpl : PredictorImpl

    private lateinit var TVScreen : ConstraintLayout
    private lateinit var TVIndicatorRound : ImageView
    private lateinit var TVIndicatorRing : ImageView
    private lateinit var TVPartOfCycle : TextView
    private lateinit var TVForecastText : TextView
    private lateinit var TVAdditionalInfo : TextView
    private lateinit var TVToday : TextView

    var selectedDate: LocalDate = LocalDate.now()
    private val today = LocalDate.now()
    private var menstruationStartDate  = LocalDate.parse("2019-10-27")
    private var menstruationEndDate  = LocalDate.parse("2019-10-29")
    private var ovulationStartDate  = LocalDate.parse("2019-11-10")
    private var ovulationEndDate  = LocalDate.parse("2019-11-20")
    private var ovulationDate = LocalDate.parse("2019-11-15")

    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val dayFormatterRound = DateTimeFormatter.ofPattern("EEEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var pref : SharedPreferences

    private var months = hashMapOf(
        "января" to "январь",
        "февраля" to "февраль",
        "марта" to "март",
        "апреля" to "апрель",
        "мая" to "май",
        "июня" to "июнь",
        "июля" to "июль",
        "августа" to "август",
        "сентября" to "сентябрь",
        "октября" to "октябрь",
        "ноября" to "ноябрь",
        "декабря" to "декабрь"
    )

    private fun setColorsOfBackgroundAndVectors(backgroundColor : Int, roundColor : Int, ringColor : Int ){
        TVScreen.setBackgroundResource(backgroundColor)
        TVIndicatorRound.setBackgroundResource(roundColor)
        TVIndicatorRing.setBackgroundResource(ringColor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_week_mode_calendar, container, false)
        TVIndicatorRound = view.findViewById(R.id.indicatorRound)
        TVIndicatorRing = view.findViewById(R.id.indicatorRing)
        TVScreen = view.findViewById<ConstraintLayout>(R.id.screen).apply {
            setOnTouchListener(OnSwipeTouchListener(context))
        }
        TVPartOfCycle = view.findViewById(R.id.TVPartOfCycle)
        TVForecastText = view.findViewById(R.id.TVForecastText)
        TVAdditionalInfo = view.findViewById(R.id.TVAdditionalInfo)
        TVToday = view.findViewById(R.id.TVToday)
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.appComponent.inject(this)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        var menstruationDays = listOf<Cycle>()
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                if (!cycleDao.getAll()[0].predicted) {
                    predictorImpl.predict(15, PreferenceManager.getDefaultSharedPreferences(context)).join()
                    predictorImpl.updateOvulation().join()
                }
                menstruationDays = cycleDao.getAll()
            }
            job.join()
        }

        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val monthText = view.MonthText
        wm.defaultDisplay.getMetrics(dm)

        calendarView.dayWidth = dm.widthPixels / 7

        calendarView.dayHeight = (calendarView.dayWidth * 1.25).toInt()
        val currentMonth = YearMonth.now()

        // Value for firstDayOfWeek does not matter since inDates and outDates are not generated.
        calendarView.setup(currentMonth.minusMonths(3), currentMonth.plusMonths(3), DayOfWeek.values().random())
        calendarView.scrollToDate(LocalDate.now().minusDays(3))

       /* (activity as MenuActivity).apply {

            btnPlusNoteWeek.setOnClickListener { view ->
                selectedDate = LocalDate.now()
                calendarPickerPresenter.addNoteFragment(supportFragmentManager, LocalDate.now())
                calendarView.notifyCalendarChanged()
                menuPresenter.addFragmentToBackStack(this@WeekModeCalendarFragment)
                menuPresenter.setFragment(supportFragmentManager, "note_redactor")
            }
        }*/


        (activity as MenuActivity).apply {
            btnTodayRound.setOnClickListener { view ->
                calendarView.scrollToDate(LocalDate.now().minusDays(3))
                selectedDate = LocalDate.now()
                calendarView.notifyCalendarChanged()
            }
            btnPlusNoteWeek.setOnClickListener { view ->
                menuPresenter.addFragmentToBackStack(this@WeekModeCalendarFragment)
                menuPresenter.setFragment(supportFragmentManager, "note_redactor")
            }
        }
        class DayViewContainer(view: View) : ViewContainer(view) {
            val dayText = view.exSevenDayText
            val dateText = view.exSevenDateText
            val selectedView = view.exSevenSelectedView
            val circleView = view.for_circle

            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    val firstDay = calendarView.findFirstVisibleDay()
                    val lastDay = calendarView.findLastVisibleDay()
                    if (firstDay == day) {
                        // If the first date on screen was clicked, we scroll to the date to ensure
                        // it is fully visible if it was partially off the screen when clicked.
                        calendarView.smoothScrollToDate(day.date)
                    } else if (lastDay == day) {
                        // If the last date was clicked, we scroll to 4 days ago, this forces the
                        // clicked date to be fully visible if it was partially off the screen.
                        // We scroll to 4 days ago because we show max of five days on the screen
                        // so scrolling to 4 days ago brings the clicked date into full visibility
                        // at the end of the calendar view.
                        calendarView.smoothScrollToDate(day.date.minusDays(4))
                    }

                    // Example: If you want the clicked date to always be centered on the screen,
                    // you would use: exSevenCalendar.smoothScrollToDate(day.date.minusDays(2))

                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        calendarView.notifyDateChanged(day.date)
                        oldDate?.let { calendarView.notifyDateChanged(it) }
                    }
                }
            }
//
            fun bind(day: CalendarDay) {
                this.day = day
                dateText.text = dateFormatter.format(day.date)
                dayText.text = dayFormatter.format(day.date)
                dateText.setTextColor(view.context.getColorCompat(R.color.colorDays))
                selectedView.isVisible = false
                if (day.date == selectedDate) {
                    if (pref.getString("language","ru").equals("ru")) {
                        monthText.text = months[monthFormatter.format(day.date)]!!.capitalize()
                    }else{
                        monthText.text = monthFormatter.format(day.date)
                    }
                }
                for (days in menstruationDays) {
                    val menstruationStartDate = LocalDate.parse(days.startOfCycle)
                    val menstruationEndDate = LocalDate.parse(days.startOfCycle)
                        .plusDays(days.lengthOfMenstruation.toLong()-1)
                    val endOfCycle = LocalDate.parse(days.startOfCycle)
                        .plusDays(days.lengthOfCycle.toLong())
                    val ovulationDate = LocalDate.parse(days.ovulation)
                    val ovulationStartDate = ovulationDate.minusDays(6)
                    val ovulationEndDate = ovulationDate.plusDays(1)
                    when (day.date) {
                        selectedDate -> {
                            dateText.setTextColor(view.context.getColorCompat(R.color.color_White))
                            selectedView.isVisible = true
                            when (selectedDate) {
                                in menstruationStartDate..menstruationEndDate -> {
                                    selectedView.setBackgroundResource(R.drawable.blob_field_selected_2)
                                    selectedView?.layoutParams?.width =
                                        selectedView?.layoutParams?.width?.minus(17)

                                    changeInformationInRound(
                                        PartOfCycle.MENSTRUATION,
                                        if (menstruationStartDate.year == selectedDate.year)
                                            selectedDate.dayOfYear - menstruationStartDate.dayOfYear + 1
                                        else 365 - menstruationStartDate.dayOfYear + selectedDate.dayOfYear + 1,
                                        day
                                    )
                                    changeColors(PartOfCycle.MENSTRUATION)
                                }
                                in ovulationStartDate..ovulationEndDate -> {
                                    if (selectedDate < ovulationDate) {
                                        selectedView.setBackgroundResource(R.drawable.orange_field_selected)
                                        changeInformationInRound(
                                            PartOfCycle.PRED_OVULATION,
                                            if (ovulationDate.year == selectedDate.year)
                                                ovulationDate.dayOfYear - selectedDate.dayOfYear
                                            else 365 - selectedDate.dayOfYear + ovulationDate.dayOfYear,
                                            day
                                        )
                                        changeColors(PartOfCycle.PRED_OVULATION)
                                    }
                                    if (selectedDate == ovulationDate) {
                                        selectedView.setBackgroundResource(R.drawable.ovulation_round_selected)
                                        changeInformationInRound(
                                            PartOfCycle.OVULATION,
                                            0,
                                            day
                                        )
                                        changeColors(PartOfCycle.OVULATION)
                                    }
                                    if ((selectedDate > ovulationDate) && (selectedDate <= ovulationEndDate)) {
                                        selectedView.setBackgroundResource(R.drawable.orange_field_selected)
                                        changeInformationInRound(
                                            PartOfCycle.POST_OVULATION,
                                            if (endOfCycle.year == selectedDate.year)
                                                endOfCycle.dayOfYear - selectedDate.dayOfYear
                                            else 365 - selectedDate.dayOfYear + endOfCycle.dayOfYear,
                                            day
                                        )
                                        changeColors(PartOfCycle.POST_OVULATION)
                                    }
                                }
                                in ovulationEndDate..endOfCycle -> {
                                    selectedView.setBackgroundResource(R.drawable.week_mode_single_selected_day)
                                    val delta = if (endOfCycle.year == selectedDate.year)
                                        endOfCycle.dayOfYear - selectedDate.dayOfYear
                                    else 365 - selectedDate.dayOfYear + endOfCycle.dayOfYear
                                    if (delta > 5) {
                                        changeInformationInRound(
                                            PartOfCycle.EMPTY_MENSTRUATION,
                                            delta,
                                            day
                                        )
                                        changeColors(PartOfCycle.EMPTY_MENSTRUATION)
                                    } else {
                                        changeInformationInRound(
                                            PartOfCycle.PRED_MENSTRUATION,
                                            delta,
                                            day
                                        )
                                        changeColors(PartOfCycle.PRED_MENSTRUATION)
                                    }
                                }
                                in menstruationEndDate..ovulationStartDate -> {
                                    selectedView.setBackgroundResource(R.drawable.week_mode_single_selected_day)
                                    changeInformationInRound(
                                        PartOfCycle.EMPTY_OVULATION,
                                        if (ovulationDate.year == selectedDate.year)
                                            ovulationDate.dayOfYear - selectedDate.dayOfYear
                                        else 365 - selectedDate.dayOfYear + ovulationDate.dayOfYear,
                                        day
                                    )
                                    changeColors(PartOfCycle.EMPTY_OVULATION)
                                }
                            }
                            if(selectedDate.isBefore(LocalDate.parse(menstruationDays[0].startOfCycle))){
                                val differenceBetweenDates = differenceBetweenDates(day.date, LocalDate.parse(menstruationDays[0].startOfCycle))
                                when(differenceBetweenDates){
                                    in 0..5 -> {
                                        changeInformationInRound(
                                            PartOfCycle.PRED_MENSTRUATION,
                                            differenceBetweenDates,
                                            day
                                        )
                                        changeColors(PartOfCycle.PRED_MENSTRUATION)
                                    }
                                    in 5..Int.MAX_VALUE -> {
                                        changeInformationInRound(
                                            PartOfCycle.EMPTY_MENSTRUATION,
                                            differenceBetweenDates,
                                            day
                                        )
                                        changeColors(PartOfCycle.EMPTY_MENSTRUATION)
                                    }
                                }
                            }

                        }
                        in menstruationStartDate..menstruationEndDate -> dateText.setTextColor(
                            view.context.getColorCompat(
                                R.color.colorOfChosenNumber
                            )
                        )
                        ovulationDate -> {
                            selectedView.isVisible = true
                            selectedView.setBackgroundResource(R.drawable.ovulation_round_not_selected)
                            dateText.setTextColor(view.context.getColorCompat(R.color.colorOfChosenNumberOrange))
                        }
                        in ovulationStartDate..ovulationEndDate -> dateText.setTextColor(
                            view.context.getColorCompat(
                                R.color.colorOfChosenNumberOrange
                            )
                        )
                    }
                }
            }
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) = container.bind(day)

        }


    }


    fun changeInformationInRound(
        indicator: PartOfCycle,
        numberOfDays: Int,
        day : CalendarDay
    ) {
        setTodayText(day)

        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION -> {

                setOtherText(numberOfDays, R.color.colorForecastTextBlue)

                setPartOfCycle(
                    R.string.menstruation_after,
                    R.color.colorPartOfCycleInRoundBlue
                )
                setAdditionalText(
                    resources.getString(R.string.unlikely_to_get_pregnant),
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.EMPTY_OVULATION -> {

                setOtherText(numberOfDays, R.color.colorForecastTextBlue)

                setPartOfCycle(
                    R.string.ovulation_after,
                    R.color.colorPartOfCycleInRoundBlue
                )
                setAdditionalText(
                    resources.getString(R.string.unlikely_to_get_pregnant),
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_MENSTRUATION -> {

                setOtherText(numberOfDays, R.color.colorForecastTextBlue)

                setPartOfCycle(
                    R.string.menstruation_after,
                    R.color.colorPartOfCycleInRoundBlue
                )

                setAdditionalText(
                    resources.getString(R.string.unlikely_to_get_pregnant),
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.PRED_OVULATION -> {

                setOtherText(numberOfDays, R.color.colorForecastTextBlue)

                setPartOfCycle(
                    R.string.ovulation_after,
                    R.color.colorPartOfCycleInRoundBlue
                )
                setAdditionalText(
                    resources.getString(R.string.average_probability_of_getting_pregnant),
                    R.color.colorPartOfCycleInRoundBlue)
            }
            PartOfCycle.MENSTRUATION -> {

                setMenstruationForecastText(numberOfDays)

                setPartOfCycle(
                    R.string.menstruation,
                    R.color.colorPartOfCycleInRoundPink
                )

                setAdditionalText(
                    "${context!!.getText(R.string.the_cycle_lasted)} \n 30 ${30.getDayAddition(context!!)}",
                    R.color.colorPartOfCycleInRoundPink)
            }
            PartOfCycle.OVULATION -> {
                setOvulationText()

                setPartOfCycle(
                    R.string.forecast_day,
                    R.color.colorPartOfCycleInRoundYellow
                    )

                setAdditionalText(
                    resources.getString(R.string.high_probability_of_getting_pregnant),
                    R.color.colorPartOfCycleInRoundYellow)
            }
            PartOfCycle.POST_OVULATION -> {

                setOtherText(numberOfDays, R.color.colorForecastTextBlue)

                setPartOfCycle(R.string.menstruation_after,
                    R.color.colorPartOfCycleInRoundYellow
                )
                setAdditionalText(
                    resources.getString(R.string.high_probability_of_getting_pregnant),
                    R.color.colorPartOfCycleInRoundBlue)
            }
        }
    }


    private fun setAdditionalText(additionalInfoText : String,
                                  additionalInfoColor: Int){
        TVAdditionalInfo.text = additionalInfoText
        TVAdditionalInfo.setTextColorRes(additionalInfoColor)
    }

    private fun setPartOfCycle(partOfCycleTextId : Int,
                               partOfCycleColor : Int){
        TVPartOfCycle.setText(partOfCycleTextId)
        TVPartOfCycle.setTextColorRes(partOfCycleColor)
    }

    private fun setTodayText(day: CalendarDay){

        TVToday.text =
            "${dayFormatterRound.format(day.date).capitalize()}, ${dateFormatter.format(day.date)} ${monthFormatter.format(
                day.date
            )}"
    }

    private fun setMenstruationForecastText(numberOfDays: Int){
        TVForecastText.text = "${resources.getString(R.string.DAY)} $numberOfDays"
        TVForecastText.setTextColorRes(R.color.colorForecastTextPink)
    }

    private fun setOvulationText(){

        TVForecastText.setText(R.string.ovulation)
        TVForecastText.setTextColorRes(R.color.colorForecastTextYellow)
    }

    private fun setOtherText(numberOfDays: Int,
                             forecastTextColor : Int){
        TVForecastText.text = "$numberOfDays ${numberOfDays.getDayAddition(context!!)}"
        TVForecastText.setTextColorRes(forecastTextColor)
    }

    fun changeCalendar(
        menstruationStart: LocalDate,
        menstruationFinish: LocalDate,
        ovulationStart:     LocalDate,
        ovulationFinish: LocalDate
    ) {
        menstruationStartDate = menstruationStart
        menstruationEndDate = menstruationFinish
        ovulationStartDate = ovulationStart
        ovulationEndDate = ovulationFinish
    }

    fun changeColors(indicator: PartOfCycle) {
        when(indicator){
            PartOfCycle.EMPTY_MENSTRUATION, PartOfCycle.EMPTY_OVULATION -> {
                setColorsOfBackgroundAndVectors(R.color.colorEmpty, R.drawable.indicator_round_empty, R.drawable.indicator_ring_empty)
            }
            PartOfCycle.PRED_MENSTRUATION ->{
                setColorsOfBackgroundAndVectors(R.drawable.gradient_pred_menstruation, R.drawable.indicator_round_pred_menstruation, R.drawable.indicator_ring_pred_menstruation)
            }
            PartOfCycle.MENSTRUATION  ->{
                setColorsOfBackgroundAndVectors(R.color.colorMenstruation, R.drawable.indicator_round_menstruation, R.drawable.indicator_ring_menstruation)
            }

            PartOfCycle.PRED_OVULATION -> {
                setColorsOfBackgroundAndVectors(R.drawable.gradient_pred_ovulation, R.drawable.indicator_round_pred_ovulation, R.drawable.indicator_ring_pred_ovulation)
            }
            PartOfCycle.OVULATION -> {
                setColorsOfBackgroundAndVectors(R.color.colorOvulation, R.drawable.indicator_round_ovulation, R.drawable.indicator_ring_ovulation)
            }
            PartOfCycle.POST_OVULATION -> {
                setColorsOfBackgroundAndVectors(R.drawable.gradient_post_ovulation, R.drawable.indicator_round_end_of_ovulation, R.drawable.indicator_ring_end_of_ovulation)
            }
        }

    }

    override fun getPart(): String = "today"

    inner class OnSwipeTouchListener(ctx: Context?) : View.OnTouchListener {
        private val gestureDetector: GestureDetector
        override fun onTouch(v: View?, event: MotionEvent?): Boolean { //обработка клика
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                var result = false
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(
                                velocityX
                            ) > SWIPE_VELOCITY_THRESHOLD
                        ) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                        }
                        result = true
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(
                            velocityY
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                    }
                    result = true
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }

            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100
        }

        fun onSwipeRight() {
            selectedDate = selectedDate.minusDays(1)
            calendarView.scrollToDate(selectedDate.minusDays(3))
            calendarView.notifyCalendarChanged()
        }
        fun onSwipeLeft() {
            selectedDate = selectedDate.plusDays(1)
            calendarView.scrollToDate(selectedDate.minusDays(3))
            calendarView.notifyCalendarChanged()
        }
        fun onSwipeTop() {}
        fun onSwipeBottom() {}

        init {
            gestureDetector = GestureDetector(ctx, GestureListener())
        }
    }

}
