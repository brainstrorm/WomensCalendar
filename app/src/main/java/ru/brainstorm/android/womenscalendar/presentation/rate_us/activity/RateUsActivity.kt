package ru.brainstorm.android.womenscalendar.presentation.rate_us.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.brainstorm.android.womenscalendar.R
import android.widget.RatingBar
import android.view.View
import kotlinx.android.synthetic.main.activity_rate_us.*


class RateUsActivity : AppCompatActivity() {


    private var mRatingBar = findViewById<View>(R.id.ratingBar) as RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_us)

        mRatingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, v, b -> }


        when (ratingBar.getRating()) {
            //1 -> mRatingScale.setText("Very bad")
            //2 -> mRatingScale.setText("Need some improvement")
            //3 -> mRatingScale.setText("Good")
            //4 -> mRatingScale.setText("Great")
            //5 -> mRatingScale.setText("Awesome. I love it")
            //else -> mRatingScale.setText("")
        }
    }
}
