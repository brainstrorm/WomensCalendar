package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.content.res.Resources
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import kotlinx.android.synthetic.main.fragment_week_mode_calendar.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.brainstorm.android.womenscalendar.App
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import ru.brainstorm.android.womenscalendar.presentation.menu.extra.PartOfCycle
import ru.brainstorm.android.womenscalendar.presentation.menu.presenter.WeekModeCalendarPresenter
import ru.brainstorm.android.womenscalendar.presentation.menu.view.WeekModeCalendarView

class WeekModeCalendarFragment : MvpAppCompatFragment(), WeekModeCalendarView {

    @InjectPresenter
    lateinit var weekModeCalendarPresenter: WeekModeCalendarPresenter

    private lateinit var TVScreen : ConstraintLayout
    private lateinit var TVIndicatorRound : ImageView
    private lateinit var TVIndicatorRing : ImageView

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
        val view = inflater.inflate(R.layout.fragment_week_mode_calendar, container, false)
        TVIndicatorRound = view.findViewById(R.id.indicatorRound)
        TVIndicatorRing = view.findViewById(R.id.indicatorRing)
        TVScreen = view.findViewById(R.id.screen)
        return view
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

    override fun changeColors(indicator: PartOfCycle) {
        when(indicator){
            PartOfCycle.EMPTY -> {
                TVScreen.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorEmpty))
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_empty)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_empty)
            }
            PartOfCycle.PRED_MENSTRUATION ->{
                TVScreen.setBackgroundResource(R.drawable.gradient_pred_menstruation)
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_pred_menstruation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_pred_menstruation)
            }
            PartOfCycle.MENSTRUATION  ->{
                TVScreen.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorMenstruation))
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_menstruation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_menstruation)
            }

            PartOfCycle.PRED_OVULATION -> {
                TVScreen.setBackgroundResource(R.drawable.gradient_pred_ovulation)
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_pred_ovulation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_pred_ovulation)
            }
            PartOfCycle.OVULATION -> {
                TVScreen.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorOvulation))
                TVIndicatorRound.setBackgroundResource(R.drawable.indicator_round_ovulation)
                TVIndicatorRing.setBackgroundResource(R.drawable.indicator_ring_ovulation)
            }
        }

    }

}
