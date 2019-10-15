package ru.brainstorm.android.womenscalendar.presentation.presenter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.fragment_average_menstruation.*
import moxy.MvpAppCompatFragment

import ru.brainstorm.android.womenscalendar.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AverageMenstruationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AverageMenstruationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AverageMenstruationFragment : MvpAppCompatFragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_average_menstruation, container, false)
        val averageMenstruationPicker = view.findViewById<NumberPicker>(R.id.averageMenstruationPicker)
        averageMenstruationPicker.minValue = 0
        averageMenstruationPicker.maxValue = 10
        return view
    }

}
