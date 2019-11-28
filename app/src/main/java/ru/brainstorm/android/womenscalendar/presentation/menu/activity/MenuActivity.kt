package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.w3c.dom.Text
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.*
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.MenuPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import java.security.AccessController.getContext
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
    private lateinit var btnToday : ImageButton
    private lateinit var btnCalendar : ImageButton
    private lateinit var btnInfo : ImageView
    private lateinit var btnMore : ImageButton
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
    @Inject
    lateinit var cycleDao: CycleDao

    companion object {

        const val TAG = "Today"

        fun provideIntent(packageContext: Context) = Intent(packageContext, MenuActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()

        topBar = findViewById(R.id.topBar)
        btnMonthOrYear = findViewById(R.id.btn_month_or_year)
        layoutLeft = findViewById<ConstraintLayout>(R.id.constraintLayout)
        layoutRight = findViewById<ConstraintLayout>(R.id.constraintLayout2)
        btnStatistics = findViewById<ImageView>(R.id.btn_statistics).apply { setOnClickListener(this@MenuActivity) }
        btnToday = findViewById<ImageButton>(R.id.today).apply{setOnClickListener(this@MenuActivity)}
        btnCalendar = findViewById<ImageButton>(R.id.calendar).apply{setOnClickListener(this@MenuActivity)}
        btnInfo = findViewById<ImageView>(R.id.info).apply{setOnClickListener(this@MenuActivity)}
        btnMore = findViewById<ImageButton>(R.id.more_text).apply{setOnClickListener(this@MenuActivity)}
        btnPlusNote = findViewById<ImageButton>(R.id.btn_plus_menu).apply { setOnClickListener(this@MenuActivity) }
        btnNewDates = findViewById<ImageButton>(R.id.btn_new_date_menu).apply { setOnClickListener(this@MenuActivity) }
        txtvwNewDates = findViewById<TextView>(R.id.btn_new_date_menu_text)
        layoutForNotes = findViewById<FrameLayout>(R.id.for_notes).apply { setOnClickListener(this@MenuActivity) }
        btnBack = findViewById<ImageView>(R.id.btn_back).apply { setOnClickListener(this@MenuActivity) }
        btnDone = findViewById<TextView>(R.id.done).apply { setOnClickListener(this@MenuActivity) }
        txtvwCalendar = findViewById<TextView>(R.id.calendar_text)
        txtvwToday = findViewById<TextView>(R.id.today_text)
        txtvwNotes = findViewById<TextView>(R.id.info_text)
        txtvwMore = findViewById<TextView>(R.id.more)
        txtvwNotesHeader = findViewById<TextView>(R.id.notes)
        btnCross = findViewById<ImageView>(R.id.btn_cross).apply { setOnClickListener(this@MenuActivity) }
        txtvwCanceled = findViewById<TextView>(R.id.btn_canceled).apply { setOnClickListener(this@MenuActivity) }
        txtvwSave = findViewById<TextView>(R.id.btn_save).apply { setOnClickListener(this@MenuActivity) }
        downBlueBar = findViewById<ConstraintLayout>(R.id.down_blue_bar)

        App.appComponent.inject(this)
        menuPresenter.setFragment(supportFragmentManager, "today")
        initFragments()


    }

    private fun initFragments(){
        menuPresenter.providePart("today")
    }
    override fun goToStatistic() {
        startActivity(StatisticsActivity.provideIntent(this@MenuActivity))
    }

    override fun setPart(part: String) {

        btnToday?.setBackgroundResource(R.drawable.ic_btn_today_menu_grey)
        btnToday?.layoutParams.width = 51
        btnToday?.layoutParams.height = 51
        txtvwToday.setTextColor(resources.getColor(R.color.colorGrey))

        btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_grey)
        btnCalendar?.layoutParams.width = 51
        btnCalendar?.layoutParams.height = 51
        txtvwCalendar.setTextColor(resources.getColor(R.color.colorGrey))

        btnInfo?.setBackgroundResource(R.drawable.ic_btn_notes_for_menu_grey)
        btnInfo?.layoutParams.width = 48
        btnInfo?.layoutParams.height = 48
        txtvwNotes.setTextColor(resources.getColor(R.color.colorGrey))

        btnMore?.setBackgroundResource(R.drawable.ic_btn_more_menu_grey)
        btnMore?.layoutParams.width = 48
        btnMore?.layoutParams.height = 48
        txtvwMore.setTextColor(resources.getColor(R.color.colorGrey))

        when(part){
            "today" ->{
                setVisibility(View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE)
                btnToday?.setBackgroundResource(R.drawable.ic_btn_today_menu_blue)
                btnToday?.layoutParams.width = 51
                btnToday?.layoutParams.height = 51
                txtvwToday.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))


            }
            "calendar" ->{
                setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE)
                btnCalendar?.setBackgroundResource(R.drawable.ic_btn_calendar_menu_blue)
                btnCalendar?.layoutParams.width = 51
                btnCalendar?.layoutParams.height = 51
                txtvwCalendar.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))


            }
            "notes" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE)
                btnInfo?.setBackgroundResource(R.drawable.ic_btn_notes_for_menu_blue)
                btnInfo?.layoutParams.width = 48
                btnInfo?.layoutParams.height = 48
                txtvwNotes.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))


            }
            "note_redactor" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE, View.GONE)
            }
            "more" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE,
                    View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE)
                btnMore?.setBackgroundResource(R.drawable.ic_btn_more_menu_blue)
                btnMore?.layoutParams.width = 48
                btnMore?.layoutParams.height = 48
                txtvwMore.setTextColor(resources.getColor(R.color.colorBlueNotesRedactor))
            }
            "change_menstruation_dates" -> {
                setVisibility(View.VISIBLE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE, View.GONE,
                    View.GONE, View.GONE, View.GONE,
                    View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
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
            R.id.more_text -> {

                //TODO
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
        }

    }

    fun setVisibility(topBarVisibility : Int, btnMonthOrYearVisibility : Int, btnStatisticsVisibility : Int,
                      btnNewDatesVisibility : Int, layoutLeftVisibility : Int, layoutRightVisibility : Int, btnPlusNoteVisibility : Int,
                      btnBackVisibility : Int, btnDoneVisibility: Int, txtvwNotesHeaderVisibility : Int,
                      btnCrossVisibility : Int, downBlueBarVisibility : Int, txtvwCanceledVisibility : Int,
                      txtvwSaveVisibility : Int){
        topBar.visibility = topBarVisibility
        btnMonthOrYear.visibility = btnMonthOrYearVisibility
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
    }
}
