package ru.brainstorm.android.womenscalendar.presentation.statistics.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.brainstorm.android.womenscalendar.R

class StatisticsActivity : AppCompatActivity() {

    /*class Adapter(private val values: List<String>): RecyclerView.Adapter<Adapter.ViewHolder>(){

    }*/
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        recyclerView = findViewById(R.id.setOfStatictics)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}
