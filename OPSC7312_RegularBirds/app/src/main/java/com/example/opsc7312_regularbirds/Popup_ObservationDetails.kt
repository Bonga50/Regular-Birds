package com.example.opsc7312_regularbirds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Popup_ObservationDetails: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //declarations
        val view = inflater.inflate(R.layout.popup_observationdetails, container, false)
        var lblTitle = view.findViewById<TextView>(R.id.lblObservationTitle)
        var lblName = view.findViewById<TextView>(R.id.lblObservationBirdName)
        var lblDate = view.findViewById<TextView>(R.id.lblObservationDate)
        var lblLocationLat = view.findViewById<TextView>(R.id.lblObservationLatitude)
        var lblLocationLng = view.findViewById<TextView>(R.id.lblObservationLongitude)

        var btnGo = view.findViewById<Button>(R.id.btnGotoHotspot)

        btnGo.setOnClickListener {
            val intent = Intent(activity, BirdNavigationActivity::class.java)
            startActivity(intent)
        }

        var tempSelectedObservation = BirdObservationHandler.getSelectedObservation()

        lblTitle.setText("Observation "+tempSelectedObservation!!.observationId.toString())
        lblName.setText("Bird name: "+tempSelectedObservation.observationName)
        lblDate.setText("Date: "+tempSelectedObservation.observationDate)
        lblLocationLat.setText("Latitude: "+tempSelectedObservation.userLocationLatitude.toString())
        lblLocationLng.setText("Longitude: "+tempSelectedObservation.userLocationLongitude.toString())

        return view
    }
}