package ru.brainstorm.android.womenscalendar.presentation.menu.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.room.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpAppCompatActivity
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.data.database.dao.CycleDao
import ru.brainstorm.android.womenscalendar.data.database.entities.Cycle
import ru.brainstorm.android.womenscalendar.presentation.menu.fragment.WeekModeCalendarFragment
import ru.brainstorm.android.womenscalendar.presentation.quiz.fragment.CalendarPickerFragment
import javax.inject.Inject

class MenuActivity : MvpAppCompatActivity() {

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
        App.appComponent.inject(this)
        var cycle1 = Cycle()
        cycle1.startOfCycle = "2019-10-27"
        cycle1.lengthOfCycle = 30
        cycle1.lengthOfMenstruation = 7
        cycle1.predicted = false
        var cycle2 = Cycle()
        cycle2.startOfCycle = "2019-11-27"
        cycle2.lengthOfCycle = 30
        cycle2.lengthOfMenstruation = 6
        cycle2.predicted = false
        GlobalScope.launch(Dispatchers.IO) {
            cycleDao.insert(cycle1)
            cycleDao.insert(cycle2)
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.for_fragment, CalendarPickerFragment())
            .commit()
    }
}
