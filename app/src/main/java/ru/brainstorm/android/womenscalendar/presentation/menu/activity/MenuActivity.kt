package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.MvpAppCompatActivity
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.CalendarPickerFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.menu.view.MenuView
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import javax.inject.Inject

class MenuActivity : MvpAppCompatActivity(), View.OnClickListener, MenuView {


    lateinit var btnStatics: ImageView
    lateinit var topBar : ImageView
    lateinit var btnMonthOrYear : ImageView
    lateinit var btnStatistics : ImageView

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
        btnStatics = findViewById(R.id.btn_statistics)

        topBar.isVisible = false
        btnMonthOrYear.isVisible = false
        btnStatics.isVisible = false

        App.appComponent.inject(this)
        supportFragmentManager.beginTransaction()
            .add(R.id.for_fragment, WeekModeCalendarFragment())
            .commit()

        btnStatics = findViewById<ImageView>(R.id.btn_statistics).apply { setOnClickListener(this@MenuActivity) }


    }

    override fun goToStatistic() {
        startActivity(StatisticsActivity.provideIntent(this@MenuActivity))
    }

    override fun onClick(v: View?) {
        v ?: return
        when(v.id) {
            R.id.btn_statistics -> {
                goToStatistic()
            }
        }

    }
}
