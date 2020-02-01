package ru.brainstorm.android.womenscalendar.presentation.menu.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Message
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ru.brainstorm.android.womenscalendar.R
import ru.brainstorm.android.womenscalendar.presentation.menu.activity.MenuActivity
import java.util.*

class ClosingOfFertilityWindowNotificationFragment : AbstractMenuFragment(), OnBackPressedListener {
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

    val TimeOfClosingOfFertilityWindowNotificationTag = "TimeOfClosingOfFertilityWindowNotification"
    val TextOfClosingOfFertilityWindowNotificationTag : String = "TextOfClosingOfFertilityWindowNotification"

    companion object{
        val TAG = "closing_of_fertility_window"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_closing_of_fertility_window_notification, container, false)

        pref = PreferenceManager.getDefaultSharedPreferences(context)

        initViews()
        initAnimators()

        updateLocale()

        return mainView
    }

    private fun initViews(){
        timeLayout = mainView!!.findViewById<ConstraintLayout>(R.id.notification_closing_of_fertility_window_time)
        timeInfoLayout = mainView!!.findViewById<ConstraintLayout>(R.id.closing_of_fertility_window_pick_time)
        timeInfoLayout.findViewById<Button>(R.id.closing_of_fertility_window_cancel_notification)
            .setOnClickListener{rollUpTimePicker()}
        timeInfoLayout.findViewById<Button>(R.id.closing_of_fertility_window_save_notification)
            .setOnClickListener{rollUpTimePicker(true)}
        messageEditText = mainView.findViewById<EditText>(R.id.message_edit)

        backButton = activity!!.findViewById<ImageView>(R.id.btn_back)
        backButton.setOnClickListener{view ->
            (activity as MenuActivity).apply {
                menuPresenter.popBackStack(supportFragmentManager)
                if (messageEditText.text.toString() != resources.getString(R.string.window_of_fertilnost_is_closing_message)) {
                    if (messageEditText.text.toString() != (resources.getString(R.string.window_of_fertilnost_is_closing_message) + " ")) {
                        pref.edit()
                            .putString(
                                TextOfClosingOfFertilityWindowNotificationTag,
                                messageEditText.text.toString()
                            )
                            .commit()
                    }
                }
            }
        }

        directTimeTextView = mainView!!.findViewById<TextView>(R.id.direct_time_text)
        directTimeTextView.text = pref.getString("time_closing_fertility","00:00")



        timePicker = mainView!!.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        txtvwTime = mainView!!.findViewById<TextView>(R.id.time_text)
        txtvwMessage = mainView!!.findViewById<TextView>(R.id.message)
    }

    private fun initAnimators() {

        val height = activity!!.windowManager.defaultDisplay.height
        val time_height = (height*3)/4

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
            pref.edit().putString("time_closing_fertility",saved).apply()

            val editor = pref.edit()
            editor.putString(TimeOfClosingOfFertilityWindowNotificationTag, saved)
            editor.commit()
        }
        val height = activity!!.windowManager.defaultDisplay.height
        val time_height = (height*3)/4
        val heightAnimator = ValueAnimator.ofInt(time_height,0).setDuration(1_000)
        heightAnimator.addUpdateListener {
            timeInfoLayout.layoutParams.height = it.animatedValue as Int
            timeInfoLayout.requestLayout()
        }
        val set = AnimatorSet()
        set.play(heightAnimator)
        set.interpolator = AccelerateDecelerateInterpolator()
        set.start()
    }
    override fun getPart(): String = "closing_of_fertility_window"

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
        messageEditText.setText(pref.getString(TextOfClosingOfFertilityWindowNotificationTag,activity!!.resources.getString(R.string.window_of_fertilnost_is_closing_message)))

    }

    override fun onBackPressed() {
        (activity as MenuActivity).apply {
            menuPresenter.popBackStack(supportFragmentManager)
            pref.edit()
                .putString(TextOfClosingOfFertilityWindowNotificationTag, messageEditText.text.toString())
                .commit()
        }
    }

}
