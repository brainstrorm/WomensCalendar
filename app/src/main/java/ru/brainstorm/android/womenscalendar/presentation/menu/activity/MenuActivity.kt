package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.OnSwipeTouchListener
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.*
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.MenuPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
import ru.brainstorm.android.womenscalendar.presentation.rate_us.activity.RateUsActivity
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import java.time.Year
import java.util.*
import javax.inject.Inject

class MenuActivity : MvpAppCompatActivity(), View.OnClickListener, MenuView{

    @InjectPresenter
    lateinit var menuPresenter: MenuPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().menuPresenter()


    private lateinit var btnStatistics: ImageView
    private lateinit var topBar : ImageView
    lateinit var btnMonthOrYear : ImageView
    var btnMonthOrYearChecked = 1
    private lateinit var btnToday : ImageButton
    private lateinit var btnCalendar : ImageButton
    private lateinit var btnInfo : ImageView
    private lateinit var btnMore : ImageView
    private lateinit var layoutLeft : ConstraintLayout
    private lateinit var layoutRight : ConstraintLayout
    private lateinit var btnPlusNote : ImageButton
    private lateinit var btnNewDates : ImageButton
    private lateinit var txtvwCalendar : TextView
    private lateinit var txtvwToday : TextView
    private lateinit var txtvwNotes : TextView
    private lateinit var txtvwMore : TextView
    private lateinit var txtvwNewDates : TextView
    private lateinit var layoutForNotes : FrameLayout
    private lateinit var btnBack : ImageView
    private lateinit var btnDone : TextView
    private lateinit var txtvwNotesHeader : TextView
    private lateinit var btnCross : ImageView
    private lateinit var txtvwCanceled : TextView
    private lateinit var txtvwSave : TextView
    private lateinit var downBlueBar : ConstraintLayout
    private lateinit var buttonToday : ImageButton
    private lateinit var buttonCalendar : ImageButton
    private lateinit var buttonInfo : ImageButton
    private lateinit var buttonMore : ImageButton
    private lateinit var btnMonthOrYearLayout : ConstraintLayout
    lateinit var txtvwMonth : TextView
    lateinit var txtvwYear : TextView
    private lateinit var txtvwChangeLanguage : TextView
    private lateinit var txtvwMyProfile : TextView
    private lateinit var txtvwNotifications : TextView
    private lateinit var txtvwStartOfMenstruation : TextView
    private lateinit var txtvwEndOfMenstruation : TextView
    private lateinit var txtvwOvulation : TextView
    private lateinit var txtvwOpeningOfFertilityWindow : TextView
    private lateinit var txtvwClosingOfFertilityWindow : TextView
    private lateinit var txtvwWomensCalendar : TextView
    private lateinit var txtvwNewDateMenu : TextView
    lateinit var btnTodayRound : ImageButton
    lateinit var btnTodayRound_2 : ImageButton
    lateinit var btnTodayRound_3 : ImageButton
    private lateinit var txtvwTodayRound : TextView
    private lateinit var txtvwAboutApp : TextView
    private lateinit var txtvwTodayBtn : TextView



    private var btnTodayIsChecked : Boolean = true
    private var btnCalendarIsChecked : Boolean = false
    private var btnInfoIsChecked : Boolean = false
    private var btnMoreIsChecked : Boolean = false
    private var currentFragment : String = ""
    //private lateinit var txtvwLanguages : TextView

    private lateinit var pref : SharedPreferences
    private lateinit var mGestureDetector: GestureDetector

    @Inject
    lateinit var cycleDao: CycleDao

    companion object {

        const val TAG = "Today"

        fun provideIntent(packageContext: Context) = Intent(packageContext, MenuActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val bar = supportActionBar
        bar!!.setBackgroundDrawable(ColorDrawable( resources.getColor(R.color.colorForActionBar)))

        supportActionBar?.hide()

        pref = PreferenceManager.getDefaultSharedPreferences(this)

        txtvwAboutApp = findViewById<TextView>(R.id.about_app)
        topBar = findViewById(R.id.topBar)
        btnMonthOrYear = findViewById<ImageView>(R.id.btn_month_or_year).apply{ setOnClickListener(this@MenuActivity)}
        layoutLeft = findViewById<ConstraintLayout>(R.id.constraintLayout)
        layoutRight = findViewById<ConstraintLayout>(R.id.constraintLayout2)
        btnStatistics = findViewById<ImageView>(R.id.btn_statistics).apply { setOnClickListener(this@MenuActivity) }
        btnToday = findViewById<ImageButton>(R.id.btn_today).apply{setOnClickListener(this@MenuActivity)}
        btnCalendar = findViewById<ImageButton>(R.id.btn_calendar).apply{setOnClickListener(this@MenuActivity)}
        btnInfo = findViewById<ImageView>(R.id.btn_info).apply{setOnClickListener(this@MenuActivity)}
        btnMore = findViewById<ImageView>(R.id.btn_more).apply{setOnClickListener(this@MenuActivity)}
        btnPlusNote = findViewById<ImageButton>(R.id.btn_plus_menu).apply { setOnClickListener(this@MenuActivity) }
        btnNewDates = findViewById<ImageButton>(R.id.btn_new_date_menu).apply { setOnClickListener(this@MenuActivity) }
        txtvwNewDates = findViewById<TextView>(R.id.btn_new_date_menu_text)
        layoutForNotes = findViewById<FrameLayout>(R.id.for_notes).apply {
            setOnTouchListener(OnSwipeTouchListener(this@MenuActivity))
        }
        btnBack = findViewById<ImageView>(R.id.btn_back).apply { setOnClickListener(this@MenuActivity) }
        btnDone = findViewById<TextView>(R.id.done).apply { setOnClickListener(this@MenuActivity) }
        txtvwCalendar = findViewById<TextView>(R.id.calendar_text)
        txtvwToday = findViewById<TextView>(R.id.today_text)
        txtvwNotes = findViewById<TextView>(R.id.info_text)
        txtvwMore = findViewById<TextView>(R.id.more_text)
        txtvwNotesHeader = findViewById<TextView>(R.id.notes)
        btnCross = findViewById<ImageView>(R.id.btn_cross).apply { setOnClickListener(this@MenuActivity) }
        txtvwCanceled = findViewById<TextView>(R.id.btn_canceled).apply { setOnClickListener(this@MenuActivity) }
        txtvwSave = findViewById<TextView>(R.id.btn_save).apply { setOnClickListener(this@MenuActivity) }
        downBlueBar = findViewById<ConstraintLayout>(R.id.down_blue_bar)
        buttonToday = findViewById<ImageButton>(R.id.today).apply { setOnClickListener(this@MenuActivity) }
        buttonCalendar = findViewById<ImageButton>(R.id.calendar).apply { setOnClickListener(this@MenuActivity) }
        buttonInfo = findViewById<ImageButton>(R.id.info).apply { setOnClickListener(this@MenuActivity) }
        buttonMore = findViewById<ImageButton>(R.id.more).apply { setOnClickListener(this@MenuActivity) }
        btnMonthOrYearLayout = findViewById(R.id.btn_month_or_year_)
        txtvwYear = findViewById<TextView>(R.id.text_year)
        txtvwMonth = findViewById<TextView>(R.id.text_month)
        txtvwChangeLanguage = findViewById(R.id.change_language)
        txtvwMyProfile = findViewById(R.id.my_profile)
        txtvwNotifications = findViewById(R.id.notifications)
        txtvwStartOfMenstruation = findViewById(R.id.start_of_menstruation)
        txtvwEndOfMenstruation = findViewById(R.id.end_of_menstruation)
        txtvwOvulation = findViewById(R.id.ovulation)
        txtvwOpeningOfFertilityWindow = findViewById(R.id.opening_of_fertility)
        txtvwClosingOfFertilityWindow = findViewById(R.id.closing_of_fertility)
        txtvwWomensCalendar = findViewById(R.id.womens_calendar)
        txtvwNewDateMenu = findViewById(R.id.btn_new_date_menu_text)
        btnTodayRound = findViewById<ImageButton>(R.id.btn_today_menu)
        btnTodayRound_2 = findViewById(R.id.btn_today_menu_2)
        btnTodayRound_3 = findViewById(R.id.btn_today_menu_3)
        txtvwTodayRound = findViewById<TextView>(R.id.today_menu)
        txtvwTodayBtn = findViewById<TextView>(R.id.today_menu)


        changeLocale(pref.getString("language", "en")!!)

        App.appComponent.inject(this)
        /*supportFragmentManager.beginTransaction()
            .add(R.id.for_fragment, CalendarYearModeFragment(), CalendarYearModeFragment.TAG)
            .commit()*/
        menuPresenter.setFragment(supportFragmentManager, "today")
        initFragments()


    }

    private fun initFragments(){
        menuPresenter.providePart("today")
    }
    override fun goToStatistic() {
        startActivity(StatisticsActivity.provideIntent(this@MenuActivity))
    }

    override fun goToRateUs() {
        startActivity(RateUsActivity.provideIntent(this@MenuActivity))
    }

    override fun setPart(part: String) {

        btnToday?.setBackgroundResource(R.drawable.ic_btn_today_menu_grey)
        btnToday?.layoutParams.width = 51
        btnToday?.layoutParams.height = 51
        txtvwToday.setTextColor(resources.getColor(R.color.colorGrey))
        btnTodayIsChecked = false

        btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_grey)
        btnCalendar?.layoutParams.width = 51
        btnCalendar?.layoutParams.height = 51
        txtvwCalendar.setTextColor(resources.getColor(R.color.colorGrey))
        btnCalendarIsChecked = false

        btnInfo?.setBackgroundResource(R.drawable.ic_btn_notes_for_menu_grey)
        btnInfo?.layoutParams.width = 48
        btnInfo?.layoutParams.height = 48
        txtvwNotes.setTextColor(resources.getColor(R.color.colorGrey))
        btnInfoIsChecked = false

        btnMore?.setBackgroundResource(R.drawable.ic_btn_more_menu_grey)
        btnMore?.layoutParams.width = 48
        btnMore?.layoutParams.height = 48
        txtvwMore.setTextColor(resources.getColor(R.color.colorGrey))
        btnMoreIsChecked = false

        currentFragment = part

        when(part){
            "today" ->{
                setVisibility(View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.VISIBLE, View.VISIBLE,View.GONE,
                    View.GONE, View.GONE)
                btnToday?.setBackgroundResource(R.drawable.ic_btn_today_menu_blue)
                btnToday?.layoutParams.width = 51
                btnToday?.layoutParams.height = 51
                txtvwToday.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnTodayIsChecked = true
                btnPlusNote.isEnabled = false
                btnTodayRound.isEnabled = true
            }
            "calendar" ->{
                setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE,View.GONE,
                    View.VISIBLE, View.GONE)
                btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_blue)
                btnCalendar?.layoutParams.width = 51
                btnCalendar?.layoutParams.height = 51
                txtvwCalendar.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnCalendarIsChecked = true
                btnPlusNote.isEnabled = false
                btnTodayRound.isEnabled = true
            }
            "notes" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnInfo?.setBackgroundResource(R.drawable.ic_btn_notes_for_menu_blue)
                btnInfo?.layoutParams.width = 48
                btnInfo?.layoutParams.height = 48
                txtvwNotes.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnInfoIsChecked = true
                btnPlusNote.isEnabled = false
                btnTodayRound.isEnabled = false
            }
            "note_redactor" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "more" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnMore?.setBackgroundResource(R.drawable.ic_btn_more_menu_blue)
                btnMore?.layoutParams.width = 48
                btnMore?.layoutParams.height = 48
                txtvwMore.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnMoreIsChecked = true
                btnPlusNote.isEnabled = false
                btnTodayRound.isEnabled = false
            }
            "change_menstruation_dates" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)

            }
            "calendar_year_mode" -> {
                setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE,View.GONE,
                    View.GONE, View.VISIBLE)
                btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_blue)
                btnCalendar?.layoutParams.width = 51
                btnCalendar?.layoutParams.height = 51
                txtvwCalendar.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnTodayRound.isEnabled = true
            }
            "languages" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "change_language" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "notifications" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }

            "start_of_menstruation" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }

            "end_of_menstruation" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "ovulation" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "opening_of_fertility_window" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "closing_of_fertility_window" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE,View.GONE,
                    View.GONE, View.GONE)
                btnTodayRound.isEnabled = false
            }
            "about_app" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE,
                    View.GONE, View.GONE)
            }
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        when(v.id) {
            R.id.btn_statistics -> {
                goToStatistic()
            }
            R.id.today -> {
               menuPresenter.setFragment(supportFragmentManager, "today")

            }
            R.id.calendar -> {
                btnMonthOrYearChecked == 1
                btnMonthOrYear.setImageResource(R.drawable.ic_toggle_button)
                txtvwMonth.setTextColorRes(R.color.white)
                txtvwYear.setTextColorRes(R.color.grey_for_year)
                menuPresenter.setFragment(supportFragmentManager, "calendar")
            }
            R.id.info -> {

                menuPresenter.setFragment(supportFragmentManager, "notes")
            }
            R.id.more -> {
                if (btnTodayIsChecked)
                    menuPresenter
                        .addFragmentToBackStack(
                            supportFragmentManager
                                .findFragmentByTag(
                                    WeekModeCalendarFragment.TAG
                                ) as AbstractMenuFragment
                        )
                if (btnCalendarIsChecked)
                    menuPresenter
                        .addFragmentToBackStack(
                            supportFragmentManager
                                .findFragmentByTag(
                                    CalendarPickerFragment.TAG
                                ) as AbstractMenuFragment
                        )
                if (btnInfoIsChecked)
                    menuPresenter
                        .addFragmentToBackStack(
                            supportFragmentManager.findFragmentByTag(
                                ListOfNotesFragment.TAG
                            ) as AbstractMenuFragment
                        )
                menuPresenter.setFragment(supportFragmentManager, "more")
            }
            R.id.btn_plus_menu, R.id.for_notes -> {
                menuPresenter.addFragmentToBackStack(
                    supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)
                            as AbstractMenuFragment)
                menuPresenter.setFragment(supportFragmentManager, "note_redactor")
            }
            R.id.done -> {
                (supportFragmentManager.findFragmentByTag(NoteRedactorFragment.TAG) as NoteRedactorFragment)
                    .apply {
                        noteRedactorPresenter.saveNote(this)
                    }

                val calendarFragment = supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)
                val notesFragment = supportFragmentManager.findFragmentByTag(ListOfNotesFragment.TAG)
                if(calendarFragment != null) {
                    supportFragmentManager.beginTransaction()
                        .detach(calendarFragment!!)
                        .attach(calendarFragment)
                        .commit()
                }
                if(notesFragment != null){
                    supportFragmentManager.beginTransaction()
                        .detach(notesFragment!!)
                        .attach(notesFragment)
                        .commit()
                }
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_back -> {
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_cross-> {
                supportFragmentManager.beginTransaction()
                    .remove(supportFragmentManager.findFragmentByTag(ChangeMenstruationDatesFragment.TAG)!!)
                    .commit()
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_canceled-> {
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_month_or_year -> {
                val calendarMonth = supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)
                val calendarYear = supportFragmentManager.findFragmentByTag(CalendarYearModeFragment.TAG)
                if (calendarMonth != null) {
                    supportFragmentManager.beginTransaction()
                        .detach(calendarMonth)
                        .attach(calendarMonth)
                        .commit()
                }
                if (calendarYear != null) {
                    supportFragmentManager.beginTransaction()
                        .detach(calendarYear)
                        .attach(calendarYear)
                        .commit()
                }
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
            R.id.btn_new_date_menu -> {
                menuPresenter.addFragmentToBackStackString(currentFragment)
                menuPresenter.setFragment(supportFragmentManager, "change_menstruation_dates")
            }
        }

    }


    fun setVisibility(topBarVisibility : Int, btnMonthOrYearVisibility : Int, btnStatisticsVisibility : Int,
                      btnNewDatesVisibility : Int, layoutLeftVisibility : Int, layoutRightVisibility : Int, btnPlusNoteVisibility : Int,
                      btnBackVisibility : Int, btnDoneVisibility: Int, txtvwNotesHeaderVisibility : Int,
                      btnCrossVisibility : Int, downBlueBarVisibility : Int, txtvwCanceledVisibility : Int,
                      txtvwSaveVisibility : Int, txtvwChangeLanguageVisibility : Int, txtvwMyProfileVisibility : Int,
                      txtvwNotificationsVisibility : Int, txtvwStartOfMenstruationVisibility : Int,
                      txtvwEndOfMenstruationVisibility : Int, txtvwWomensCalendarVisibility : Int,
                      txtvwOvulationVisibility : Int, txtvwOpeningFertilityWindowVisibility : Int,
                      txtvwClosingOfFertilityWindowVisibility : Int, btnTodayRoundVisibility: Int, txtvwTodayRoundVisibility : Int,txtvwAboutAppVisibility : Int,
                      btnTodayRound_2Visibility : Int, btnTodayRound_3Visibility : Int){
        txtvwAboutApp.visibility = txtvwAboutAppVisibility
        topBar.visibility = topBarVisibility
        btnMonthOrYearLayout.visibility = btnMonthOrYearVisibility
        btnStatistics.visibility = btnStatisticsVisibility
        btnNewDates.visibility = btnNewDatesVisibility
        txtvwNewDates.visibility = btnNewDatesVisibility
        layoutLeft.visibility = layoutLeftVisibility
        layoutRight.visibility = layoutRightVisibility
        btnPlusNote.visibility = btnPlusNoteVisibility
        btnBack.visibility = btnBackVisibility
        btnDone.visibility = btnDoneVisibility
        txtvwNotesHeader.visibility = txtvwNotesHeaderVisibility
        btnCross.visibility = btnCrossVisibility
        downBlueBar.visibility = downBlueBarVisibility
        txtvwCanceled.visibility = txtvwCanceledVisibility
        txtvwSave.visibility = txtvwSaveVisibility
        txtvwChangeLanguage.visibility = txtvwChangeLanguageVisibility
        txtvwMyProfile.visibility = txtvwMyProfileVisibility
        txtvwNotifications.visibility = txtvwNotificationsVisibility
        txtvwStartOfMenstruation.visibility = txtvwStartOfMenstruationVisibility
        txtvwEndOfMenstruation.visibility = txtvwEndOfMenstruationVisibility
        txtvwWomensCalendar.visibility = txtvwWomensCalendarVisibility
        txtvwOvulation.visibility = txtvwOvulationVisibility
        txtvwOpeningOfFertilityWindow.visibility = txtvwOpeningFertilityWindowVisibility
        txtvwClosingOfFertilityWindow.visibility = txtvwClosingOfFertilityWindowVisibility
        btnTodayRound.visibility = btnTodayRoundVisibility
        txtvwTodayRound.visibility = txtvwTodayRoundVisibility
        btnTodayRound_2.visibility = btnTodayRound_2Visibility
        btnTodayRound_3.visibility = btnTodayRound_3Visibility
    }

    fun changeLocale(language: String){
        val APP_LANGUAGE = "language"
        val editor = pref.edit()
        editor.putString(APP_LANGUAGE, language)
        editor.commit()
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwToday.setText(R.string.today)
        txtvwCalendar.setText(R.string.calendar)
        txtvwNotes.setText(R.string.notes)
        txtvwMore.setText(R.string.more)
        txtvwNotesHeader.setText(R.string.notes)
        txtvwCanceled.setText(R.string.cancel)
        txtvwSave.setText(R.string.btn_save_menu)
        txtvwYear.setText(R.string.btn_year)
        txtvwMonth.setText(R.string.btn_month)
        txtvwChangeLanguage.setText(R.string.change_language)
        txtvwNewDateMenu.setText(R.string.new_date_text_menu)

        txtvwMyProfile.setText(R.string.my_profile)
        txtvwNotifications.setText(R.string.notifications)
        txtvwStartOfMenstruation.setText(R.string.start_of_menstruation)
        txtvwEndOfMenstruation.setText(R.string.end_of_menstruation)
        txtvwWomensCalendar.setText(R.string.womens_calendar)
        txtvwOvulation.setText(R.string.ovulation_notification)
        txtvwOpeningOfFertilityWindow.setText(R.string.opening_of_fertility_window)
        txtvwClosingOfFertilityWindow.setText(R.string.closing_of_fertility_window)
        txtvwTodayBtn.setText(R.string.text_today_menu)
    }

    override fun onBackPressed() {
        when(currentFragment){
            "start_of_menstruation",
            "end_of_menstruation",
            "ovulation",
            "opening_of_fertility_window",
            "closing_of_fertility_window" -> {
                val fragment = supportFragmentManager.findFragmentByTag(currentFragment)
                as OnBackPressedListener
                fragment.onBackPressed()
            }
            else -> {
                menuPresenter.popBackStack(supportFragmentManager)
            }
        }
    }

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
                menuPresenter.addFragmentToBackStack(supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG) as AbstractMenuFragment)
                menuPresenter.setFragment(supportFragmentManager, "note_redactor")
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

        fun onSwipeRight() {}
        fun onSwipeLeft() {}
        fun onSwipeTop() {}
        fun onSwipeBottom(){
            supportFragmentManager.beginTransaction()
                .detach(supportFragmentManager.findFragmentByTag(SelectedDayNoteFragment.TAG)!!)
                .commit()
        }

        init {
            gestureDetector = GestureDetector(ctx, GestureListener())
        }
    }

    //
}