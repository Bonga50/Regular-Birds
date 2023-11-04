package com.example.opsc7312_regularbirds

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.Date

class AddNewObservationFragment : Fragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_new_observation, container, false)

        var txtBirdName = view.findViewById<TextView>(R.id.txtBirdName)
        var btnCreateObsrevation = view.findViewById<Button>(R.id.btnCreateNew)
        var btnClear = view.findViewById<Button>(R.id.btnClear)

        btnClear.setOnClickListener {
            txtBirdName.setText("")
        }
        btnCreateObsrevation.setOnClickListener {
            if (txtBirdName!=null){
                BirdObservationHandler.addObservation(
                    BirdObservationModel(
                        observationId=BirdObservationHandler.generateObservationId(),
                        observationName= "Observation "+txtBirdName.text.toString(),
                        observationDate= LocalDate.now().toString(),
                        userLocationLongitude = BirdHotspots.userLongitude,
                        userLocationLatitude = BirdHotspots.userLatitude,
                        UserId= UserHandler.getVerifiedUser()!!
                    )
                )
            }
        }

        return view
    }


}