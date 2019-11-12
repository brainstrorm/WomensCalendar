package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.ListOfNotesFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.MenuPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import javax.inject.Inject

class MenuActivity : MvpAppCompatActivity(), View.OnClickListener, MenuView {

    @InjectPresenter
    lateinit var menuPresenter: MenuPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().menuPresenter()


    lateinit var btnStatistics: ImageView
    lateinit var topBar : ImageView
    lateinit var btnMonthOrYear : ImageView
    private lateinit var btnToday : ImageButton
    //private lateinit var btnTodayText : TextView
    private lateinit var btnCalendar : ImageButton
    //private lateinit var btnCalendarText : TextView
    private lateinit var btnInfo : ImageButton
    //private lateinit var btnInfoText : TextView
    private lateinit var btnMore : ImageButton
    //private lateinit var btnMoreText : TextView

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
        btnStatistics = findViewById<ImageView>(R.id.btn_statistics).apply { setOnClickListener(this@MenuActivity) }
        btnToday = findViewById<ImageButton>(R.id.today).apply{setOnClickListener(this@MenuActivity)}
        //btnTodayText = findViewById<TextView>(R.id.today_text).apply{setOnClickListener(this@MenuActivity)}
        btnCalendar = findViewById<ImageButton>(R.id.calendar).apply{setOnClickListener(this@MenuActivity)}
        //btnCalendarText = findViewById<TextView>(R.id.calendar_text).apply{setOnClickListener(this@MenuActivity)}
        btnInfo = findViewById<ImageButton>(R.id.info).apply{setOnClickListener(this@MenuActivity)}
        //btnInfoText = findViewById<TextView>(R.id.info_text).apply{setOnClickListener(this@MenuActivity)}
        btnMore = findViewById<ImageButton>(R.id.more_text).apply{setOnClickListener(this@MenuActivity)}
        //btnMoreText = findViewById<TextView>(R.id.more).apply{setOnClickListener(this@MenuActivity)}

        App.appComponent.inject(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.for_fragment, WeekModeCalendarFragment())
            .commit()
        initFragments()


    }

    private fun initFragments(){
        menuPresenter.providePart("today")
    }
    override fun goToStatistic() {
        startActivity(StatisticsActivity.provideIntent(this@MenuActivity))
    }

    override fun setPart(part: String) {
        when(part){
            "today" ->{
                setVisibility(View.GONE, View.GONE, View.GONE)
            }
            "calendar" ->{
                setVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE)
            }
            "notes" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE)
            }
            "more" ->{
                setVisibility(View.VISIBLE, View.GONE, View.GONE)
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
        }

    }

    fun setVisibility(topBarVisibility : Int, btnMonthOrYearVisibility : Int, btnStatisticsVisibility : Int){
        topBar.visibility = topBarVisibility
        btnMonthOrYear.visibility = btnMonthOrYearVisibility
        btnStatistics.visibility = btnStatisticsVisibility
    }
}
