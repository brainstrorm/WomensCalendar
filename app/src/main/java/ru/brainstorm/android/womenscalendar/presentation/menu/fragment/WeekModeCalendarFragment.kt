package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App

import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.WeekModeCalendarPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.WeekModeCalendarView

class WeekModeCalendarFragment : MvpAppCompatFragment(), WeekModeCalendarView {

    @InjectPresenter
    lateinit var weekModeCalendarPresenter: WeekModeCalendarPresenter



    @ProvidePresenter
    fun providePresenter() = App.appComponent.presenter().weekModeCalendarPresenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_mode_calendar, container, false)
    }


    private var weekDays = HashMap<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weekDays.put("Mon", "Пн")
        weekDays.put("Tue", "Вт")
        weekDays.put("Wed", "Ср")
        weekDays.put("Thu", "Чт")
        weekDays.put("Fri", "Пт")
        weekDays.put("Sat", "Сб")
        weekDays.put("Sun", "Вс")


    }

    override fun changeInformationInRound(
        partOfCycle: String,
        forecast: String,
        additionalInfo: String
    ) {

    }

    override fun changePeriods(
        menstruationStart: Int,
        menstruationFinish: Int,
        ovulationStart: Int,
        ovulationFinish: Int
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeColors(indicator: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
