package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
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
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.*
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.MenuPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.setTextColorRes
import ru.brainstorm.android.womenscalendar.presentation.rate_us.activity.RateUsActivity
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import java.util.*
import javax.inject.Inject

class MenuActivity : MvpAppCompatActivity(), View.OnClickListener, MenuView {

    @InjectPresenter
    lateinit var menuPresenter: MenuPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().menuPresenter()


    private lateinit var btnStatistics: ImageView
    private lateinit var topBar : ImageView
    private lateinit var btnMonthOrYear : ImageView
    private  var btnMonthOrYearChecked = 1
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
    private lateinit var txtvwMonth : TextView
    private lateinit var txtvwYear : TextView
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

    private var btnTodayIsChecked : Boolean = true
    private var btnCalendarIsChecked : Boolean = false
    private var btnInfoIsChecked : Boolean = false
    private var btnMoreIsChecked : Boolean = false

    //private lateinit var txtvwLanguages : TextView

    private lateinit var pref : SharedPreferences

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
        changeLocale(pref.getString("language", "en")!!)

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
        layoutForNotes = findViewById<FrameLayout>(R.id.for_notes).apply { setOnClickListener(this@MenuActivity) }
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

        when(part){
            "today" ->{
                setVisibility(View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
                btnToday?.setBackgroundResource(R.drawable.ic_btn_today_menu_blue)
                btnToday?.layoutParams.width = 51
                btnToday?.layoutParams.height = 51
                txtvwToday.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnTodayIsChecked = true
                btnPlusNote.isEnabled = false
            }
            "calendar" ->{
                setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
                btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_blue)
                btnCalendar?.layoutParams.width = 51
                btnCalendar?.layoutParams.height = 51
                txtvwCalendar.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnCalendarIsChecked = true
                btnPlusNote.isEnabled = false
            }
            "notes" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
                btnInfo?.setBackgroundResource(R.drawable.ic_btn_notes_for_menu_blue)
                btnInfo?.layoutParams.width = 48
                btnInfo?.layoutParams.height = 48
                txtvwNotes.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnInfoIsChecked = true
                btnPlusNote.isEnabled = false
            }
            "note_redactor" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
            }
            "more" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
                btnMore?.setBackgroundResource(R.drawable.ic_btn_more_menu_blue)
                btnMore?.layoutParams.width = 48
                btnMore?.layoutParams.height = 48
                txtvwMore.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
                btnMoreIsChecked = true
                btnPlusNote.isEnabled = false
            }
            "change_menstruation_dates" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
            }
            "calendar_year_mode" -> {
                setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
                btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_blue)
                btnCalendar?.layoutParams.width = 51
                btnCalendar?.layoutParams.height = 51
                txtvwCalendar.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
            }
            "languages" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE)
            }
            "change_language" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
            }
            "notifications" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
            }

            "start_of_menstruation" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
            }

            "end_of_menstruation" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE,
                    View.GONE, View.GONE, View.GONE)
            }
            "ovulation" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.GONE, View.GONE)
            }
            "opening_of_fertility_window" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.GONE)
            }
            "closing_of_fertility_window" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.VISIBLE)
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
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_back -> {
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_new_date_menu -> {
                menuPresenter.setFragment(supportFragmentManager, "change_menstruation_dates")
                var calendarPickerFragment : AbstractMenuFragment? = supportFragmentManager.findFragmentByTag(CalendarPickerFragment.TAG)
                    as AbstractMenuFragment?
                var weekModeCalendarFragment : AbstractMenuFragment? = supportFragmentManager.findFragmentByTag(WeekModeCalendarFragment.TAG)
                    as AbstractMenuFragment?
                if(calendarPickerFragment != null && calendarPickerFragment.isHidden == false)
                    menuPresenter.addFragmentToBackStack(calendarPickerFragment)
                if(weekModeCalendarFragment != null && weekModeCalendarFragment.isHidden == false)
                    menuPresenter.addFragmentToBackStack(weekModeCalendarFragment)
            }
            R.id.btn_cross, R.id.btn_canceled-> {
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_save -> {
                val changeMenstruationDatesFragment = supportFragmentManager.findFragmentByTag(
                    ChangeMenstruationDatesFragment.TAG
                ) as ChangeMenstruationDatesFragment
                changeMenstruationDatesFragment.apply {
                    changeMenstruationDatesPresenter.save(getStartDate(), getEndDate(), supportFragmentManager)
                }
                menuPresenter.popBackStack(supportFragmentManager)
            }
            R.id.btn_month_or_year -> {
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

    fun setVisibility(topBarVisibility : Int, btnMonthOrYearVisibility : Int, btnStatisticsVisibility : Int,
                      btnNewDatesVisibility : Int, layoutLeftVisibility : Int, layoutRightVisibility : Int, btnPlusNoteVisibility : Int,
                      btnBackVisibility : Int, btnDoneVisibility: Int, txtvwNotesHeaderVisibility : Int,
                      btnCrossVisibility : Int, downBlueBarVisibility : Int, txtvwCanceledVisibility : Int,
                      txtvwSaveVisibility : Int, txtvwChangeLanguageVisibility : Int, txtvwMyProfileVisibility : Int,
                      txtvwNotificationsVisibility : Int, txtvwStartOfMenstruationVisibility : Int,
                      txtvwEndOfMenstruationVisibility : Int, txtvwWomensCalendarVisibility : Int,
                      txtvwOvulationVisibility : Int, txtvwOpeningFertilityWindowVisibility : Int,
                      txtvwClosingOfFertilityWindowVisibility : Int){
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
        var txtvwToday = findViewById<TextView>(R.id.today_text)
        var txtvwCalendar = findViewById<TextView>(R.id.calendar_text)
        var txtvwNotes = findViewById<TextView>(R.id.info_text)
        var txtvwMore = findViewById<TextView>(R.id.more_text)
        var txtvwNotesHeader = findViewById<TextView>(R.id.notes)
        var txtvwCanceled = findViewById<TextView>(R.id.btn_canceled)
        var txtvwSave = findViewById<TextView>(R.id.btn_save)
        var txtvwYear = findViewById<TextView>(R.id.text_year)
        var txtvwMonth = findViewById<TextView>(R.id.text_month)
        var txtvwChangeLanguage = findViewById<TextView>(R.id.change_language)
        var txtvwNewDateMenu = findViewById<TextView>(R.id.btn_new_date_menu_text)

        var txtvwMyProfile = findViewById<TextView>(R.id.my_profile)
        var txtvwNotifications = findViewById<TextView>(R.id.notifications)
        var txtvwStartOfMenstruation = findViewById<TextView>(R.id.start_of_menstruation)
        var txtvwEndOfMenstruation = findViewById<TextView>(R.id.end_of_menstruation)
        var txtvwOvulation = findViewById<TextView>(R.id.ovulation)
        var txtvwOpeningOfFertilityWindow = findViewById<TextView>(R.id.opening_of_fertility)
        var txtvwClosingOfFertilityWindow = findViewById<TextView>(R.id.closing_of_fertility)
        var txtvwWomensCalendar = findViewById<TextView>(R.id.womens_calendar)

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
    }
}
