package com.example.opsc7312_regularbirds

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.resecondsense_v01.Gamification
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {
    var tempTravelDist:Int=0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_settings, container, false)

        val seekBar = view.findViewById<SeekBar>(R.id.sbMaxDistance)
        val lblTravelDist = view.findViewById<TextView>(R.id.lblSettingsMaxNumber)
        val radioGroup = view.findViewById<RadioGroup>(R.id.unitOfMeasurementRadioGroup)
        val btnGamification =view.findViewById<TextView>(R.id.lblGamification)
        lblTravelDist.setText(UserHandler.userSettingsDefault.Distance.toString())
        seekBar.setProgress(UserHandler.userSettingsDefault.Distance,true)
        if(UserHandler.userSettingsDefault.UnitOfMeasurment=="Metric"){
            radioGroup.check(R.id.radioOptionMetric)
        }else{
            radioGroup.check(R.id.radioOptionImperial)
        }
        //Setting for Max Distance
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                lblTravelDist.setText(progress.toString())
                tempTravelDist=progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // This method is called when the user starts changing the SeekBar value
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
               UserHandler.updateDistanceInFirebase(tempTravelDist)
                UserHandler.userSettingsDefault.Distance=tempTravelDist
                lifecycleScope.launch {
                    UserHandler.getSettingsFromFirebse()
                }
            }

        })

        //setting for unit of measurement
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioOptionImperial -> {
                    UserHandler.updateUnitOfMeasurmentInFirebase("Imperial")
                    UserHandler.userSettingsDefault.UnitOfMeasurment="Imperial"
                }
                R.id.radioOptionMetric -> {
                    UserHandler.updateUnitOfMeasurmentInFirebase("Metric")
                    UserHandler.userSettingsDefault.UnitOfMeasurment="Metric"
                }

            }
            lifecycleScope.launch {
                UserHandler.getSettingsFromFirebse()
            }
        }

        //Gamification
        btnGamification.setOnClickListener {
            val intent = Intent(activity, Gamification::class.java)
            startActivity(intent)
        }

        return view
    }


}