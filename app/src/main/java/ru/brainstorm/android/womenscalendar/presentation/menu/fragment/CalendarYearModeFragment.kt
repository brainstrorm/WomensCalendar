package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.library.YearView
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.time.Year


class CalendarYearModeFragment : AbstractMenuFragment(){
    override fun getPart(): String {
        return "calendar_year_mode"
    }

    companion object{
        val TAG = "CalendarYearMode"

        private val HARDCODED_YEAR_COUNT = 10
        private val START_YEAR = 2019
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerView = inflater.inflate(R.layout.year_fragment, container, false) as RecyclerView
        recyclerView.adapter = YearAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (activity as MenuActivity).btnTodayRound.setOnClickListener {
            recyclerView.scrollToPosition(Year.now().value - START_YEAR)
        }
        return recyclerView
    }

    private inner class YearViewHolder(view: View): RecyclerView.ViewHolder(view)

    private inner class YearAdapter : RecyclerView.Adapter<YearViewHolder>() {

        //This count will be hardcoded
        override fun getItemCount(): Int {
            return HARDCODED_YEAR_COUNT
        }

        override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
            val view = holder.itemView.findViewById<YearView>(R.id.year_view)
            view.adapter.year = Year.of(START_YEAR + position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
            val yearView = layoutInflater.inflate(R.layout.year_item, parent, false)
            return YearViewHolder(yearView)
        }
    }
}
