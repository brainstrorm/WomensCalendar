package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.util.*

class OvulationNotificationFragment : AbstractMenuFragment(), OnBackPressedListener {
    private lateinit var mainView : View
    private lateinit var backButton : ImageView
    private lateinit var timeLayout : ConstraintLayout
    private lateinit var timeInfoLayout: ConstraintLayout
    private lateinit var directTimeTextView : TextView
    private lateinit var timePicker : TimePicker
    private lateinit var messageEditText: EditText
    private lateinit var pref : SharedPreferences

    private lateinit var txtvwTime : TextView
    private lateinit var txtvwMessage: TextView

    val TimeOfOvulationNotificationTag = "TimeOfOvulationNotification"
    val TextOfOvulationNotificationTag : String = "TextOfOvulationNotification"

    companion object{
        val TAG = "ovulation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_ovulation_notification, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        initViews()
        initAnimators()

        updateLocale()

        return mainView
    }

    private fun initViews(){
        timeLayout = mainView!!.findViewById<ConstraintLayout>(R.id.notification_ovulation_time)
        timeInfoLayout = mainView!!.findViewById<ConstraintLayout>(R.id.ovulation_pick_time)
        timeInfoLayout.findViewById<Button>(R.id.ovulation_cancel_notification)
            .setOnClickListener{rollUpTimePicker()}
        timeInfoLayout.findViewById<Button>(R.id.ovulation_save_notification)
            .setOnClickListener{rollUpTimePicker(true)}
        messageEditText = mainView.findViewById<EditText>(R.id.message_edit)

        backButton = activity!!.findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener{view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
                pref.edit()
                    .putString(TextOfOvulationNotificationTag, messageEditText.text.toString())
                    .commit()
            }
        }

        directTimeTextView = mainView!!.findViewById<TextView>(R.id.direct_time_text)
        directTimeTextView.text = pref.getString("time_ovulation","00:00")
        messageEditText.setText(pref.getString(TextOfOvulationNotificationTag,activity!!.resources.getString(R.string.ovulation_will_stsrt_message)))


        timePicker = mainView!!.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        txtvwTime = mainView!!.findViewById<TextView>(R.id.time_text)
        txtvwMessage = mainView!!.findViewById<TextView>(R.id.message)
    }

    private fun initAnimators() {

        val height = activity!!.windowManager.defaultDisplay.height
        val time_height = (1300*height/2037)

        timeLayout.setOnClickListener {
            val heightAnimator = ValueAnimator.ofInt(0, time_height).setDuration(1_000)
            heightAnimator.addUpdateListener {
                val value = it.animatedValue as Int
                timeInfoLayout.layoutParams.height = value
                timeInfoLayout.requestLayout()
            }

            val set = AnimatorSet()
            set.play(heightAnimator)
            set.interpolator = AccelerateDecelerateInterpolator()
            set.start()
        }
    }

    private fun rollUpTimePicker(save: Boolean = false) {
        if (save) {
            val saved = "${timePicker.hour}:${timePicker.minute}"
            directTimeTextView.text = saved
            pref.edit().putString("time_ovulation",saved).apply()

            val editor = pref.edit()
            editor.putString(TimeOfOvulationNotificationTag, saved)
            editor.commit()
        }
        val height = activity!!.windowManager.defaultDisplay.height
        val time_height = (1300*height/2037)
        val heightAnimator = ValueAnimator.ofInt(time_height, 0).setDuration(1_000)
        heightAnimator.addUpdateListener {
            timeInfoLayout.layoutParams.height = it.animatedValue as Int
            timeInfoLayout.requestLayout()
        }
        val set = AnimatorSet()
        set.play(heightAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.start()
    }
    override fun getPart(): String = "ovulation"

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun updateLocale(){
        val language = pref.getString("language", "en")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        getResources().updateConfiguration(configuration, null)

        txtvwTime.setText(R.string.time)
        txtvwMessage.setText(R.string.message)
    }

    override fun onBackPressed() {
        (activity as MenuActivity).apply {
            menuPresenter.popBackStack(supportFragmentManager)
            pref.edit()
                .putString(TextOfOvulationNotificationTag, messageEditText.text.toString())
                .commit()
        }
    }

}
