package ru.brainstorm.android.womenscalendar.presentation.rate_us.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.brainstorm.android.womenscalendar.R
import android.widget.RatingBar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_rate_us.*
import ru.brainstorm.android.womenscalendar.presentation.statistics.activity.StatisticsActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri


class RateUsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "RateUs"

        fun provideIntent(packageContext: Context) = Intent(packageContext, RateUsActivity::class.java)
    }

    private lateinit var mRatingBar: RatingBar
    private lateinit var cancel: TextView
    private lateinit var txtvwCallBack : TextView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_us)
        val bar = supportActionBar
        bar!!.setBackgroundDrawable(ColorDrawable( resources.getColor(R.color.colorForActionBar)))


        mRatingBar = findViewById<RatingBar>(R.id.ratingBar)
        cancel = findViewById<TextView>(R.id.cancel)
        txtvwCallBack = findViewById<TextView>(R.id.call_back)
        txtvwCallBack.setOnClickListener {
            sendEmail()
        }
        mRatingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, v, b ->

                when (ratingBar.getRating().toInt()) {
                    1 -> launchMarket()
                    2 -> launchMarket()
                    3 -> launchMarket()
                    4 -> launchMarket()
                    5 -> launchMarket()
                }

            }

        supportActionBar?.hide()


        cancel.setOnClickListener{
            finish()
        }
    }

    fun sendEmail(){
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("mobappcloud@gmail.com"))
        //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email")
        //i.putExtra(Intent.EXTRA_TEXT, "body of email")
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun launchMarket() {
        val uri = Uri.parse("market://details?id=com.ivy.jewel.mystery")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
        }

    }

}
